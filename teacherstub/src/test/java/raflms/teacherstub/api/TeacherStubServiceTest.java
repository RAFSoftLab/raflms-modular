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
        boolean rez = service.addTest("ispitavgustoop", LocalDate.of(2026,9,2),"OOP","ispit");
        assertTrue(rez);
    }


    @Test
    public void testAddAssignment(){

        boolean rez = service.addAssigment( "ispitavgustoop","grupa2","termin",
                "/home/bojana/Documents/nastava/ООП/ispitavgust/pakovanje/OOP-ispitavgust-grupa2");
        assertTrue(rez);


    }



}