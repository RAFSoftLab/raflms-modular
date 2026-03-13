package raflms.teacherstub.restclient;

import org.springframework.web.client.RestClient;
import raflms.teacherstub.config.TeacherStubConfig;
import raflms.teacherstub.dtos.StudentForTestRequest;
import raflms.teacherstub.dtos.StudentInfoDTO;
import raflms.teacherstub.dtos.StudentsForTest;

public class StudentRestClient {

    private TeacherStubConfig restConfig;
    private RestClient restClient;

    private final String STUDENT_URL_PATH = "/student";

    public StudentRestClient(String baseURL) {
        this.restConfig = restConfig;
        restClient = RestClient.builder()
                .baseUrl(baseURL+STUDENT_URL_PATH)
                .build();
    }

    public Long addNewStudent(StudentInfoDTO st){
       return restClient.post()
                .uri("/add")
                .body(st)
                .retrieve()
                .body(Long.class);
    }

    public boolean registerStudentForTest(StudentForTestRequest st){
        return restClient.post()
                .uri("/registerfortest")
                .body(st)
                .retrieve()
                .body(Boolean.class);
    }


    public boolean registerStudentsForTest(StudentsForTest st){
        return restClient.post()
                .uri("/registerallfortest")
                .body(st)
                .retrieve()
                .body(Boolean.class);
    }

}
