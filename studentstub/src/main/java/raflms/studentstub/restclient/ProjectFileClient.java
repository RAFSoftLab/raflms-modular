package raflms.studentstub.restclient;


import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import raflms.studentstub.config.StudentStubConfig;


import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ProjectFileClient {

    private StudentStubConfig restConfig;
    private RestClient restClient;

    private final String PROJECT_URL_PATH = "/project";

    public ProjectFileClient(String baseURL) {
        this.restConfig = restConfig;
        restClient = RestClient.builder()
                .baseUrl(baseURL+PROJECT_URL_PATH)
                .build();
    }

    public Boolean uploadFile(String localFilePath, String remoteRepoPath) {

        File file = new File(localFilePath);
        Resource fileResource = new FileSystemResource(file);


        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add("file", fileResource);
        body.add("repoPath", remoteRepoPath);


        Boolean responseBody = restClient.post()
                .uri("/upload")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(Boolean.class);

        return responseBody;
    }


    public String downloadFile(String assignmentRepoPath, String projectRoot){
        Resource resource = restClient.get()
                .uri("/download?filePath={param1}", assignmentRepoPath)
                .retrieve()
                .body(Resource.class);

        try (InputStream in = resource.getInputStream()) {
            String zadatakPath = projectRoot+"/zadatak.zip";
            Files.copy(in, Path.of(zadatakPath), StandardCopyOption.REPLACE_EXISTING);
            return zadatakPath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
