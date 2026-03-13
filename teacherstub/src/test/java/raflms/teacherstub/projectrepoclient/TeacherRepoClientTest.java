package raflms.teacherstub.projectrepoclient;

import org.junit.jupiter.api.Test;
import raflms.teacherstub.projectrepoclient.impl.FileRepoClient;

class TeacherRepoClientTest {

    @Test
    public void testPushAssignment(){
        TeacherRepoClient gitTeacherClient = new FileRepoClient();
        gitTeacherClient.pushAssignment("/home/bojana/raflms/projectrootdir/OOP/mojoop/grupa2/termin1","/home/bojana/RAFProjects/projectfolder/dbexporter-test");
    }

}