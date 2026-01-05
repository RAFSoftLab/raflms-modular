package raflms.studentstub.restclient;

import org.springframework.web.client.RestClient;
import raflms.studentstub.dtos.StudentAssignmentResponse;
import raflms.studentstub.dtos.StudentStartAssignmentRequest;

public class AssigmentRestClient {


    private final RestClient restClient;

    private final String SUBMISSION_URL_PATH = "/student/submission";

    public  AssigmentRestClient(String baseURL) {
        restClient = RestClient.builder()
                .baseUrl(baseURL+SUBMISSION_URL_PATH)
                .build();
    }

    public StudentAssignmentResponse startAssignment(StudentStartAssignmentRequest s){
        return restClient.post()
                .uri("/authorizeforasignment")
                .body(s)
                .retrieve()
                .body(StudentAssignmentResponse.class);
    }
}