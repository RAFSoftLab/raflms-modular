package raflms.studentstub.api;


import raflms.studentstub.config.StudentStubConfig;
import raflms.studentstub.dtos.StudentAssignmentResponse;
import raflms.studentstub.dtos.StudentStartAssignmentRequest;
import raflms.studentstub.exceptions.NotAllowedToSubmitProject;
import raflms.studentstub.repoclient.StudentRepoClient;
import raflms.studentstub.repoclient.impl.FileRepoClient;
import raflms.studentstub.restclient.AssigmentRestClient;

public class StudentStubService {


    private final StudentStubConfig config;
    private final AssigmentRestClient assRestClient;
    private final StudentRepoClient studentRepoClient;

    private String loggedStudentRepoPath = null;
    private String loggedStudentToken = null;
    private String projectRoot = null;


    public StudentStubService(StudentStubConfig config) {
        this.config = config;
        assRestClient = new AssigmentRestClient(config.getBaseApiURL());
        studentRepoClient = new FileRepoClient();
    }


    public boolean startAssigment(int indexNumber, String startYear, String studyProgramShortName, String studentGroup, String testName, String group, String term, String projectRoot){
        StudentStartAssignmentRequest request = new StudentStartAssignmentRequest(indexNumber, startYear, studyProgramShortName, studentGroup, testName, group, term);
        StudentAssignmentResponse response = assRestClient.startAssignment(request);
        if(response!=null) {
            this.loggedStudentToken = response.getToken();
            this.loggedStudentRepoPath = response.getStudentFolderPath();
            this.projectRoot = projectRoot;

        }else{
            // TODO log
            return false;
        }
        return studentRepoClient.retrieveAssignmentProject(response.getAssignmentPath(),projectRoot);

    }

    public boolean submitAssignment(Boolean isFinalSubmission) throws NotAllowedToSubmitProject {
        if(loggedStudentRepoPath==null){
            throw new NotAllowedToSubmitProject("Student nije prijavljen, ne može da pošalje zadatak");
        }
        if(isFinalSubmission) {
            boolean ok = studentRepoClient.submitAssignmentProject(loggedStudentRepoPath, projectRoot, true);
            if(ok) {
                this.loggedStudentRepoPath = null;
                this.loggedStudentToken = null;
            }
            return ok;
        }else{
            return studentRepoClient.submitAssignmentProject(loggedStudentRepoPath, projectRoot, false);
        }
    }

    // privremeno ovde stoji zbog testiranja
    public void setLoggedStudentRepoPath(String path) {
        this.loggedStudentRepoPath = path;
    }

    public String getLoggedStudentRepoPath() {
        return loggedStudentRepoPath;
    }

    public void setProjectRoot(String projectRoot) {
        this.projectRoot = projectRoot;
    }
}
