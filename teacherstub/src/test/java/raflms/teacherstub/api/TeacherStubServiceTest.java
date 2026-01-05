package raflms.teacherstub.api;

import org.junit.jupiter.api.Test;
import raflms.teacherstub.config.TeacherStubConfig;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TeacherStubServiceTest {

    private static final TeacherStubConfig config = ConfigFactory.createConfig();
    private static final TeacherStubService service = new TeacherStubService(config);


    @Test
    public void testAddTest(){
        boolean rez = service.addTest("mojsupertest", LocalDate.of(2026,6,13),"OOP","ispit");
        assertTrue(rez);
    }


    @Test
    public void testAddAssignment(){

        boolean rez = service.addAssigment( "mojsupertest","grupa4","prvi termin",
                "/home/bojana/Documents/projekti/zadatak");
        assertTrue(rez);


    }



}