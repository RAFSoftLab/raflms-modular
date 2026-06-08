package raflms.studentstub.api;

import org.junit.jupiter.api.Test;
import raflms.studentstub.api.datamodel.TestWithAssignments;
import raflms.studentstub.config.ConfigFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
                7,
                "2020",
                "RN",
                "101",
                "koloop",
                "grupa1",
                "termin1",
                "/home/bojana/raflms/studentprojectroot");
        System.out.println(studentService.getLoggedStudentRepoPath());
        assertTrue(ok);
    }


    @Test
    public void testStartAssignemntWithName() throws IOException, InterruptedException {
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
                20,
                "2025",
                "RN",
                "101",
                "Marko",
                "Markovic",
                "koloop",
                "grupa1",
                "termin1",
                "/home/bojana/raflms/studentprojectroot");
        System.out.println(studentService.getLoggedStudentRepoPath());
        assertTrue(ok);
    }

    @Test
    public void submitAssignment(){
        studentService.setLoggedStudentRepoPath("/home/user/raflms/projectsrootdir/OOP/koloop/grupa1/termin1/studentrepos/RN-7-2020-Zika-Zikic(101)");
        studentService.setProjectRoot("/home/bojana/raflms/studentprojectroot");
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