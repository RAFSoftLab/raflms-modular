package raflms.teacherstub.restclient;

import org.junit.jupiter.api.Test;
import raflms.teacherstub.config.ConfigFactory;
import raflms.teacherstub.dtos.TestDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestRestClientTestIT {


    @Test
    public void testConnection(){

        TestRestClient testClient = new TestRestClient(ConfigFactory.createConfig().getBaseApiURL());
        List<TestDTO> tests = testClient.getAllTest();
        for(TestDTO t:tests){
            System.out.println(t.getTestName());
        }
        assertFalse(tests.isEmpty());
    }

}