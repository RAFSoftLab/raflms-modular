package raflms.studentstub.api;

import org.junit.jupiter.api.Test;
import raflms.studentstub.config.ConfigFactory;

import static org.junit.jupiter.api.Assertions.*;

class StudentStubServiceTest {

    private final StudentStubService studentService = new StudentStubService(ConfigFactory.createConfig());

    @Test
    public void testStartAssignemnt(){
        boolean ok = studentService.startAssigment(7,"2020","RN","102","testoop","grupa1","termin1","/home/bojana/raflms/studentprojectroot");
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