package raflms.teacherstub.restclient;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import raflms.teacherstub.config.TeacherStubConfig;
import raflms.teacherstub.dtos.AssignmentRequest;
import raflms.teacherstub.dtos.AssignmentResponse;
import raflms.teacherstub.dtos.TestDTO;

import java.util.List;

public class TestRestClient {



    private final RestClient restClient;

    private final String TEST_URL_PATH = "/test";

    public TestRestClient(String baseURL) {
        restClient = RestClient.builder()
                .baseUrl(baseURL+TEST_URL_PATH)
                .build();
    }

    public List<TestDTO> getAllTest(){
        return restClient.get()
                .uri("/all")
                .retrieve()
                .body(new ParameterizedTypeReference<List<TestDTO>>() {});
    }

    public Boolean addTest(TestDTO t){
        return restClient.post()
                .uri("/add")
                .body(t)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Boolean.class);

    }

    public AssignmentResponse addAssignment(AssignmentRequest ass){
        return restClient.post()
                .uri("/addassignment")
                .body(ass)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(AssignmentResponse.class);

    }


}
