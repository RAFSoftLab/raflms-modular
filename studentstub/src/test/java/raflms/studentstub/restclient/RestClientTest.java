package raflms.studentstub.restclient;

import org.junit.jupiter.api.Test;
import raflms.studentstub.config.ConfigFactory;
import raflms.studentstub.dtos.AssignmentResponse;
import raflms.studentstub.dtos.TestDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestClientTest {

    private final TestRestClient testRestClient = new TestRestClient(ConfigFactory.createConfig().getBaseApiURL());

    @Test
    public void testGetAllTests(){
        List<TestDTO> tests = testRestClient.getAllTest();
        assertFalse(tests.isEmpty());
        for(TestDTO t:tests){
            System.out.println(t.getTestName());
        }
    }

    @Test
    public void testGetAssignments(){
        List<AssignmentResponse> ass = testRestClient.getAssignmentsForTestName("testoop");
        for(AssignmentResponse as:ass){
            System.out.println(as.toString());
        }
    }

}