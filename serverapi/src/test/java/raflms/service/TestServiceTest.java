package raflms.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import raflms.dtos.AssignmentRequest;
import raflms.dtos.AssignmentResponse;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestServiceTest {

    @Autowired
    private TestService testService;

    @Test
    public void testAddAssignment(){

        AssignmentRequest asReq = new AssignmentRequest("grupa1","prvi termin","mojprvitestOOP");
        AssignmentResponse asRes = testService.addAssignment(asReq);
        assertNotNull(asRes);

    }


}