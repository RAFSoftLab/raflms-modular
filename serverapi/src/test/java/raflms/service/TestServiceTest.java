package raflms.service;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import raflms.dtos.AssignmentRequest;
import raflms.dtos.AssignmentResponse;
import raflms.dtos.SubjectDTO;
import raflms.dtos.TestDTO;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TestServiceTest {

    @Autowired
    private TestService testService;

    @Autowired
    private SubjectService subjectService;

    @Order(1)
    @Test
    @Disabled
    public void testAddSubject(){
        Long rez = subjectService.addIfNotExists(new SubjectDTO("Objektno-orijentisano programiranje","OOP"));
        assertNotNull(rez);


    }

    @Order(2)
    @Test
    @Disabled
    public void testAddTest(){
        TestDTO test = new TestDTO("mojoop", LocalDate.of(2026,4,5),"OOP","kolokvijum");
        boolean rez = testService.addTest(test);
        assertTrue(rez);

    }

    @Order(3)
    @Test
    @Disabled
    public void testAddAssignment(){

        AssignmentRequest asReq = new AssignmentRequest("grupa2","termin1","mojoop");
        AssignmentResponse asRes = testService.addAssignment(asReq);
        assertNotNull(asRes);

    }


}