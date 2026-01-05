package raflms.teacherstub.restclient;

import org.junit.jupiter.api.Test;
import raflms.teacherstub.api.ConfigFactory;
import raflms.teacherstub.config.TeacherStubConfig;
import raflms.teacherstub.dtos.TestDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestRestClientTestIT {


    @Test
    public void testConnection(){

        TestRestClient testClient = new TestRestClient(ConfigFactory.createConfig().getBaseApiURL());
        List<TestDTO> tests = testClient.getAllTest();
        assertFalse(tests.isEmpty());
    }

}