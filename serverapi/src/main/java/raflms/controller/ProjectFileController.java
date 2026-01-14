package raflms.controller;


import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import raflms.service.TestService;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/project")
public class ProjectFileController {

    private static final Logger log = LoggerFactory.getLogger(ProjectFileController.class);

    private final TestService testService;

    public ProjectFileController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/upload")
    public Boolean uploadFile(@RequestParam String repoPath, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            log.error("Upload file failed, empty file");
            return false;
        }
        try {
            FileUtils.cleanDirectory(new File(repoPath));
            byte[] bytes = file.getBytes();
            String newRepoPath = repoPath + "/" + file.getOriginalFilename();
            Path path = Paths.get(newRepoPath);
            Files.write(path, bytes);
            log.info("File successfully uploaded " + file.getOriginalFilename());
            testService.updateRepoPath(repoPath, newRepoPath);
            return true;

        } catch (Exception e) {
            log.error("Upload file failed, empty file:  "+e.getMessage());
            return false;
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filePath) {

        File file = new File(filePath);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            log.error(String.format("File on path $s not found",filePath));
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
