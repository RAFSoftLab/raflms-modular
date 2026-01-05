package raflms.studentstub.api;

import raflms.studentstub.config.StudentStubConfig;
import raflms.studentstub.dtos.StudentAssignmentResponse;
import raflms.studentstub.dtos.StudentStartAssignmentRequest;
import raflms.studentstub.gitclient.GitStudentClient;
import raflms.studentstub.restclient.AssigmentRestClient;

public class StudentStubService {

    private final StudentStubConfig config;
    private final AssigmentRestClient assRestClient;
    private final GitStudentClient gitClient;

    public StudentStubService(StudentStubConfig config) {
        this.config = config;
        assRestClient = new AssigmentRestClient(config.getBaseApiURL());
        gitClient = new GitStudentClient();
    }

    public boolean startAssigment(StudentStartAssignmentRequest request, String projectRoot){
        StudentAssignmentResponse response = assRestClient.startAssignment(request);
        boolean ok = gitClient.cloneAssigmentRepo(response.getGitForkPath(),projectRoot);
        return ok;
    }

}
