package raflms.studentstub.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.zip.ZipUtil;
import raflms.studentstub.config.StudentStubConfig;
import raflms.studentstub.dtos.StudentAssignmentResponse;
import raflms.studentstub.dtos.StudentStartAssignmentRequest;
import raflms.studentstub.repoclient.StudentRepoClient;
import raflms.studentstub.repoclient.impl.FileRepoClient;
import raflms.studentstub.repoclient.impl.GitStudentClient;
import raflms.studentstub.restclient.AssigmentRestClient;

import java.io.File;

public class StudentStubService {


    private final StudentStubConfig config;
    private final AssigmentRestClient assRestClient;
    private final StudentRepoClient studentRepoClient;

    private String loggedStudentRepoPath = null;
    private String loggedStudentToken = null;

    public StudentStubService(StudentStubConfig config) {
        this.config = config;
        assRestClient = new AssigmentRestClient(config.getBaseApiURL());
        studentRepoClient = new FileRepoClient();
    }

    public boolean startAssigment(StudentStartAssignmentRequest request, String projectRoot){
        StudentAssignmentResponse response = assRestClient.startAssignment(request);
        if(response!=null) {
            loggedStudentToken = response.getToken();
            loggedStudentRepoPath = response.getStudentFolderPath();
        }else{
            // TODO log
            return false;
        }

        String assignmetFilePath = studentRepoClient.retrieveAssignmentProject(response.getAssignmentPath(),projectRoot);
        File assignmentZipFile = new File(assignmetFilePath);
        ZipUtil.unpack(assignmentZipFile, new File(projectRoot));
        assignmentZipFile.delete();
        return true;
    }

}
