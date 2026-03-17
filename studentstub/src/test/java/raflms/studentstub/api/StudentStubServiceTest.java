package raflms.studentstub.api;

import org.junit.jupiter.api.Test;
import raflms.studentstub.api.datamodel.TestWithAssignments;
import raflms.studentstub.config.ConfigFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentStubServiceTest {

    private final StudentStubService studentService = new StudentStubService(ConfigFactory.createConfig());

    @Test
    public void testStartAssignemnt() throws IOException, InterruptedException {
//        boolean ok = studentService.startAssigment(
//                7,
//                "2020",
//                "RN",
//                "102",
//                "testoop",
//                "grupa1",
//                "termin1",
//                "/Users/lukamitrovic/Desktop/Doktorske/tests/rafrootproject");
        boolean ok = studentService.startAssigment(
                6,
                "2025",
                "DN",
                "102",
                "testoop",
                "grupa1",
                "termin1",
                "/Users/lukamitrovic/Desktop/Doktorske/tests/rafrootproject");
        System.out.println(studentService.getLoggedStudentRepoPath());
        assertTrue(ok);
    }

    @Test
    public void submitAssignment(){
        studentService.setLoggedStudentRepoPath("/home/user/raflms/projectsrootdir/OOP/testoop/grupa1/termin1/studentrepos/7c5d91dd-f398-41ba-b1b3-6cf6c0d4d0ba");
        studentService.setProjectRoot("/Users/lukamitrovic/Desktop/Doktorske/tests/rafrootproject");
        boolean ok = studentService.submitAssignment(true);
        assertTrue(ok);
    }

    @Test
    public void test_getAllTestsWithAssignemnts(){
        List<TestWithAssignments> rez = studentService.getAllTestsWithAssigmentsData();
        System.out.println(rez);
        assertFalse(rez.isEmpty());
    }


}