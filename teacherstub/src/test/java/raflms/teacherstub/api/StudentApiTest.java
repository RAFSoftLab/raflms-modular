package raflms.teacherstub.api;

import org.junit.jupiter.api.Test;
import raflms.teacherstub.config.ConfigFactory;
import raflms.teacherstub.config.TeacherStubConfig;
import raflms.teacherstub.dtos.StudentInfoDTO;
import raflms.teacherstub.dtos.StudentsForTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentApiTest {

    private static final TeacherStubConfig config = ConfigFactory.createConfig();
    private static final TeacherStubService service = new TeacherStubService(config);


    @Test
    public void testRegisterStudentForTest(){
        boolean rez = service.registerStudentForTest("mojsupertest","Mirko","Mirkovic",4,"RN","2019");
        assertTrue(rez);
    }

    @Test
    public void testRegisterStudentsForTest(){
        StudentsForTest st = new StudentsForTest("testoop");
        StudentInfoDTO st1 = new StudentInfoDTO("Pera", "Peric", 5, "2020","RN");
        StudentInfoDTO st2 = new StudentInfoDTO("Mika", "Mikic", 6, "2021","RN");
        StudentInfoDTO st3 = new StudentInfoDTO("Zika", "Zikic", 7, "2020","RN");
        st.setStudents(List.of(st1,st2,st3));
        boolean rez = service.registerStudentsForTest(st);
        assertTrue(rez);
    }
}
