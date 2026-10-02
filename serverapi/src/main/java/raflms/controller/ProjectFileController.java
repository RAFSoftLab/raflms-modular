package raflms.controller;


import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import raflms.conf.RAFLMSProperties;
import raflms.service.StudentSubmissionService;
import raflms.service.TestService;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/project")
public class ProjectFileController {

    private static final Logger log = LoggerFactory.getLogger(ProjectFileController.class);

    private final TestService testService;
    private final StudentSubmissionService studentSubmissionService;
    private final RAFLMSProperties raflmsProperties;

    public ProjectFileController(TestService testService, StudentSubmissionService studentSubmissionService,
                                  RAFLMSProperties raflmsProperties) {
        this.testService = testService;
        this.studentSubmissionService = studentSubmissionService;
        this.raflmsProperties = raflmsProperties;
    }

    /**
     * RISK-02 fix: validates that the client-supplied repoPath lies inside the configured
     * project root directory, preventing path-traversal attacks that could delete or overwrite
     * arbitrary server files (e.g. repoPath=/etc or repoPath=/../../../).
     *
     * @throws SecurityException if the resolved path escapes the project root
     */
    private Path validateRepoPath(String repoPath) throws IOException {
        Path root = Paths.get(raflmsProperties.getProjectrootdir()).toRealPath();
        Path target = Paths.get(repoPath).normalize();
        // toRealPath() requires the path to exist; use normalize + startsWith as the guard.
        if (!target.startsWith(root)) {
            log.error("Path traversal attempt blocked: repoPath='{}' is outside root='{}'", repoPath, root);
            throw new SecurityException("Nedozvoljena putanja: " + repoPath);
        }
        return target;
    }

    @PostMapping("/upload/assignment")
    public Boolean uploadAssignemntFile(@RequestParam String repoPath, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            log.error("Upload assignment file failed, empty file");
            return false;
        }
        try {
            Path safeRepoPath = validateRepoPath(repoPath); // RISK-02 fix
            FileUtils.cleanDirectory(safeRepoPath.toFile());
            byte[] bytes = file.getBytes();
            Path path = safeRepoPath.resolve(file.getOriginalFilename()).normalize();
            if (!path.startsWith(safeRepoPath)) { // double-check filename doesn't escape dir
                log.error("Suspicious filename rejected: {}", file.getOriginalFilename());
                return false;
            }
            Files.write(path, bytes);
            log.info("File successfully uploaded " + file.getOriginalFilename());
            testService.updateRepoPath(repoPath, path.toString());
            return true;

        } catch (SecurityException e) {
            log.error("Upload assignment blocked — path traversal: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Upload file failed:  "+e.getMessage());
            return false;
        }
    }

    @PostMapping("/upload/studentproject")
    public Boolean uploadStudentProjectFile(@RequestParam String repoPath, @RequestParam Boolean isFinalSubmission, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            log.error("Upload student project file failed, empty file");
            return false;
        }
        try {
            Path safeRepoPath = validateRepoPath(repoPath); // RISK-02 fix
            FileUtils.cleanDirectory(safeRepoPath.toFile());
            byte[] bytes = file.getBytes();
            Path path = safeRepoPath.resolve(file.getOriginalFilename()).normalize();
            if (!path.startsWith(safeRepoPath)) { // double-check filename doesn't escape dir
                log.error("Suspicious filename rejected: {}", file.getOriginalFilename());
                return false;
            }
            Files.write(path, bytes);
            log.info("File successfully uploaded " + file.getOriginalFilename());
            studentSubmissionService.setSubmissionTimeForRepoPath(repoPath);
            return true;

        } catch (SecurityException e) {
            log.error("Upload student project blocked — path traversal: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Upload file failed: {}", e.getMessage());
            return false;
        }
    }

    // RISK-01 fix: GET /project/download?filePath= removed — accepted arbitrary server paths
    // from the client (path traversal / arbitrary file read). Use /download/studentassignment/{id}
    // which resolves the path server-side from the database.

    @GetMapping("/download/studentassignment/{id}")
    public ResponseEntity<Resource> downloadStudentAssignment(@PathVariable Long id) {
        String filePath = studentSubmissionService.getRepoPathForStudentSubmissionId(id);
        if(filePath==null) {
            log.error(String.format("Student nije predao rad"));
            return null;
        }
        File fileDir = new File(filePath);

        if (!fileDir.exists() && !fileDir.isDirectory()) {
            return ResponseEntity.notFound().build();
        }

        File file = fileDir.listFiles()[0]; // trebalo bi da ima samo jedan file


        Resource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            log.error(String.format("File on path %s not found",filePath));
            return null;
        }

        MediaType mediaType = MediaType.TEXT_PLAIN; // Example


        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, mediaType.toString());
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(mediaType)
                .contentLength(file.length())
                .body(resource);
    }

}
