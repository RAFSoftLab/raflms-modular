package raflms.studentstub.restclient;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import raflms.studentstub.dtos.AssignmentResponse;
import raflms.studentstub.dtos.TestDTO;

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

    public List<AssignmentResponse> getAssignmentsForTestName(String testName){
        return  restClient.get()
                .uri("/{testName}/assignments", testName)
                .retrieve()
                .body(new ParameterizedTypeReference<List<AssignmentResponse>>() {});

    }



}
