package raflms.studentstub.api;

import org.junit.jupiter.api.Test;
import raflms.studentstub.dtos.StudentStartAssignmentRequest;

import static org.junit.jupiter.api.Assertions.*;

class StudentStubServiceTest {

    private final StudentStubService studentService = new StudentStubService(ConfigFactory.createConfig());

    @Test
    public void testStartAssignemnt(){
        StudentStartAssignmentRequest sr = new StudentStartAssignmentRequest(7,"2020","RN","102","testoop","grupa4","termin1");
        boolean ok = studentService.startAssigment(sr,"/home/bojana/raflms/studentprojectroot");
        System.out.println(studentService.getLoggedStudentRepoPath());
        assertTrue(ok);
    }

    @Test
    public void submitAssignment(){
        studentService.setLoggedStudentRepoPath("/home/user/raflms/projectsrootdir/OOP/testoop/grupa4/termin1/studentrepos/94114516-f102-409f-a4f9-ef15205cb982");
        studentService.setProjectRoot("/home/bojana/raflms/studentprojectroot");
        boolean ok = studentService.submitAssignment(true);
        assertTrue(ok);
    }



}