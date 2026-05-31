package raflms.teacherstub.api;

import org.junit.jupiter.api.Test;
import raflms.teacherstub.config.ConfigFactory;
import raflms.teacherstub.config.TeacherStubConfig;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TeacherStubServiceTest {

    private static final TeacherStubConfig config = ConfigFactory.createConfig();
    private static final TeacherStubService service = new TeacherStubService(config);


    @Test
    public void testAddTest(){
        boolean rez = service.addTest("koloop", LocalDate.of(2026,4,6),"OOP","kolokvijum");
        assertTrue(rez);
    }


    @Test
    public void testAddAssignment(){

        boolean rez = service.addAssigment( "koloop","grupa1","termin1",
                "/home/bojana/Documents/nastava/ООП/kolokvijum/2026/kol1");
        assertTrue(rez);


    }



}