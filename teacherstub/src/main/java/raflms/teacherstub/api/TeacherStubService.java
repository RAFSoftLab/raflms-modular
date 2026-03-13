package raflms.teacherstub.api;

import raflms.teacherstub.config.TeacherStubConfig;
import raflms.teacherstub.dtos.*;
import raflms.teacherstub.projectrepoclient.TeacherRepoClient;
import raflms.teacherstub.projectrepoclient.impl.FileRepoClient;
import raflms.teacherstub.restclient.StudentRestClient;
import raflms.teacherstub.restclient.TestRestClient;

import java.time.LocalDate;

public class TeacherStubService {

    private final TeacherStubConfig config;
    private final TestRestClient testRestClient;
    private final StudentRestClient studentRestClient;
    private final TeacherRepoClient repoClient;

    public TeacherStubService(TeacherStubConfig config) {
        this.config = config;
        this.testRestClient = new TestRestClient(config.getBaseApiURL());
        this.studentRestClient = new StudentRestClient(config.getBaseApiURL());
        this.repoClient = new FileRepoClient();
    }


    public boolean addAssigment(String testName, String group, String term, String projectRootDir){
        AssignmentRequest assReq = new AssignmentRequest(group, term, testName);
        AssignmentResponse assRes = testRestClient.addAssignment(assReq);
        repoClient.pushAssignment(assRes.getGitRepoPath(),projectRootDir);
        return true;
    }

    public boolean addTest(String testName, LocalDate testDate, String subjectShortName, String testType){
        TestDTO test = new TestDTO(testName, testDate, subjectShortName, testType);
        boolean ok = testRestClient.addTest(test);
        return ok;
    }

    public boolean registerStudentForTest(String testName, String studentFirstName, String studentLastName, Integer studentIndexNum, String studyProgramShortName, String startYear){
        StudentForTestRequest st = new StudentForTestRequest(studentFirstName, studentLastName, studentIndexNum, startYear, studyProgramShortName, testName);
        boolean ok = studentRestClient.registerStudentForTest(st);
        return ok;
    }

    public boolean registerStudentsForTest(StudentsForTest st){
        boolean ok = studentRestClient.registerStudentsForTest(st);
        return ok;
    }
}
