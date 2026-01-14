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
        studentService.setLoggedStudentRepoPath("/home/bojana/raflms/remote/projectfolder/OOP/testoop/grupa4/termin1/studentrepos/4c527381-491e-4f7b-afec-2f77e6061269");
        studentService.setProjectRoot("/home/bojana/raflms/studentprojectroot");
        boolean ok = studentService.submitAssignment(true);
        assertTrue(ok);
    }



}