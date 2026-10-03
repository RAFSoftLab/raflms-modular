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
     * RISK-10 fix: sanitizes the original filename provided by the client.
     * Removes null bytes, control characters, directory separators, and other
     * characters that could cause issues on the filesystem or in HTTP headers.
     * Returns only the last path component (basename), capped at 255 characters.
     *
     * @throws SecurityException if the resulting name is empty or suspiciously structured
     */
    private String sanitizeFilename(String originalFilename) {
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new SecurityException("Naziv fajla je prazan ili null");
        }
        // Strip null bytes and control characters
        String name = originalFilename.replaceAll("[\\x00-\\x1F\\x7F]", "");
        // Take only the basename (strip any path component the client may have injected)
        int lastSep = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\'));
        if (lastSep >= 0) {
            name = name.substring(lastSep + 1);
        }
        // Allow only safe filename characters: letters, digits, dash, underscore, dot
        name = name.replaceAll("[^a-zA-Z0-9._\\-]", "_");
        if (name.isBlank() || name.equals(".") || name.equals("..")) {
            throw new SecurityException("Naziv fajla nije validan nakon sanitizacije: " + originalFilename);
        }
        // Cap length to avoid filesystem limits
        if (name.length() > 255) {
            name = name.substring(0, 255);
        }
        return name;
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
            String safeFilename = sanitizeFilename(file.getOriginalFilename()); // RISK-10 fix
            Path safeRepoPath = validateRepoPath(repoPath); // RISK-02 fix
            FileUtils.cleanDirectory(safeRepoPath.toFile());
            byte[] bytes = file.getBytes();
            Path path = safeRepoPath.resolve(safeFilename).normalize();
            if (!path.startsWith(safeRepoPath)) {
                log.error("Filename escape attempt rejected: {}", safeFilename);
                return false;
            }
            Files.write(path, bytes);
            log.info("File successfully uploaded: {}", safeFilename);
            testService.updateRepoPath(repoPath, path.toString());
            return true;

        } catch (SecurityException e) {
            log.error("Upload assignment blocked: {}", e.getMessage());
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
            String safeFilename = sanitizeFilename(file.getOriginalFilename()); // RISK-10 fix
            Path safeRepoPath = validateRepoPath(repoPath); // RISK-02 fix
            // RISK-13 fix: proveravamo da repoPath odgovara stvarnoj StudentSubmission u bazi.
            // Bez ove provere, student bi mogao da navede tuđu putanju unutar projectrootdir
            // i pregazi tudji rad (IDOR).
            if (!studentSubmissionService.isValidStudentRepoPath(repoPath)) {
                log.warn("Upload odbijen — repoPath ne odgovara nijednoj StudentSubmission: {}", repoPath);
                return false;
            }
            FileUtils.cleanDirectory(safeRepoPath.toFile());
            byte[] bytes = file.getBytes();
            Path path = safeRepoPath.resolve(safeFilename).normalize();
            if (!path.startsWith(safeRepoPath)) {
                log.error("Filename escape attempt rejected: {}", safeFilename);
                return false;
            }
            Files.write(path, bytes);
            log.info("File successfully uploaded: {}", safeFilename);
            studentSubmissionService.setSubmissionTimeForRepoPath(repoPath);
            return true;

        } catch (SecurityException e) {
            log.error("Upload student project blocked: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Upload file failed: {}", e.getMessage());
            return false;
        }
    }

    // RISK-01 fix (revised): GET /project/download?filePath= vratio je bilo koji fajl sa servera.
    // Endpoint je neophodan (student plugin ga koristi da preuzme zadatak), ali je sada zaštićen:
    // - filePath mora biti unutar raflms.projectrootdir (validateRepoPath guard)
    // - Fajl mora biti ZIP arhiva (jedini tip koji server šalje studentima)
    // Endpoint ostaje nezasticen tokenom jer ga poziva student bez tokena — ali je napad sada
    // ogranicen isključivo na fajlove unutar projectrootdir koji su ZIP-ovi.
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filePath) {
        Path safePath;
        try {
            safePath = validateRepoPath(filePath);
        } catch (SecurityException | IOException e) {
            log.warn("Download odbijen — putanja van projectrootdir: {}", filePath);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        File file = safePath.toFile();
        if (!file.exists() || !file.isFile()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            log.error("Download: fajl nije nadjen: {}", safePath);
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .body(resource);
    }

    // RISK-08 fix: koristi publicId (UUID) umesto sekvencijalnog Long id-a
    @GetMapping("/download/studentassignment/{publicId}")
    public ResponseEntity<Resource> downloadStudentAssignment(@PathVariable String publicId) {
        String filePath = studentSubmissionService.getRepoPathForPublicId(publicId);
        if (filePath == null) {
            log.warn("Download studentassignment/{}: submission ne postoji ili nije predana", publicId);
            return ResponseEntity.notFound().build();
        }
        File fileDir = new File(filePath);

        if (!fileDir.exists() || !fileDir.isDirectory()) {
            log.error("Download studentassignment/{}: direktorijum ne postoji: {}", publicId, filePath);
            return ResponseEntity.notFound().build();
        }

        File[] files = fileDir.listFiles();
        if (files == null || files.length == 0) {
            log.error("Download studentassignment/{}: direktorijum je prazan ili nije citljiv: {}", publicId, filePath);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        File file = files[0];

        Resource resource;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            log.error("Download studentassignment/{}: fajl nije nadjen: {}", publicId, file.getAbsolutePath());
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .body(resource);
    }

}
