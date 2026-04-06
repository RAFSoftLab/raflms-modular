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
        boolean rez = service.addTest("vezbeoop", LocalDate.of(2026,4,6),"OOP","vezbe");
        assertTrue(rez);
    }


    @Test
    public void testAddAssignment(){

        boolean rez = service.addAssigment( "vezbeoop","grupa1","termin2",
                "/home/bojana/Documents/nastava/ООП/projekti/zadaci/zad2");
        assertTrue(rez);


    }



}