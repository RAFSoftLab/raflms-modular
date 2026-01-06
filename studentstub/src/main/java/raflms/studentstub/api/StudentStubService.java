package raflms.studentstub.api;

import raflms.studentstub.config.StudentStubConfig;
import raflms.studentstub.dtos.StudentAssignmentResponse;
import raflms.studentstub.dtos.StudentStartAssignmentRequest;
import raflms.studentstub.repoclient.StudentRepoClient;
import raflms.studentstub.repoclient.impl.FileRepoClient;
import raflms.studentstub.repoclient.impl.GitStudentClient;
import raflms.studentstub.restclient.AssigmentRestClient;

public class StudentStubService {

    private final StudentStubConfig config;
    private final AssigmentRestClient assRestClient;
    private final StudentRepoClient studentRepoClient;

    public StudentStubService(StudentStubConfig config) {
        this.config = config;
        assRestClient = new AssigmentRestClient(config.getBaseApiURL());
        studentRepoClient = new FileRepoClient();
    }

    public boolean startAssigment(StudentStartAssignmentRequest request, String projectRoot){
        StudentAssignmentResponse response = assRestClient.startAssignment(request);
        boolean ok = studentRepoClient.retrieveAssignmentProject(response.getStudentRepoPath(),projectRoot);
        return ok;
    }

}
