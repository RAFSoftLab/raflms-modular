package raflms.studentstub.api;

import org.junit.jupiter.api.Test;
import raflms.studentstub.dtos.StudentStartAssignmentRequest;

import static org.junit.jupiter.api.Assertions.*;

class StudentStubServiceTest {

    private final StudentStubService studentService = new StudentStubService(ConfigFactory.createConfig());

    @Test
    public void testStartAssignemnt(){
        StudentStartAssignmentRequest sr = new StudentStartAssignmentRequest(7,"2020","RN","102","testoop","grupa1","termin1");
        boolean ok = studentService.startAssigment(sr,"/home/bojana/RAFProjects/studentprojectroot");
        assertTrue(ok);

    }

}