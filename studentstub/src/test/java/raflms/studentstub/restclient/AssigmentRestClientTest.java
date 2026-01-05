package raflms.studentstub.restclient;

import org.junit.jupiter.api.Test;
import raflms.studentstub.api.ConfigFactory;
import raflms.studentstub.dtos.StudentAssignmentResponse;
import raflms.studentstub.dtos.StudentStartAssignmentRequest;

class AssigmentRestClientTest {

    private final AssigmentRestClient restClient = new AssigmentRestClient(ConfigFactory.createConfig().getBaseApiURL());

    @Test
    public void startAssignment(){
        StudentStartAssignmentRequest sr = new StudentStartAssignmentRequest(6,"2021","RN","102","mojprvitestOOP","grupa1","termin1");
        StudentAssignmentResponse response = restClient.startAssignment(sr);
        System.out.println(response);
    }

}