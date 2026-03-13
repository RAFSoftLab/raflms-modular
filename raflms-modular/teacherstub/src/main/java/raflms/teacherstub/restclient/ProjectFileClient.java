package raflms.teacherstub.restclient;


import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import raflms.teacherstub.config.TeacherStubConfig;

import java.io.File;

public class ProjectFileClient {

    private TeacherStubConfig restConfig;
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
                .uri("/upload/assignment")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(Boolean.class);

        return responseBody;
    }
}
