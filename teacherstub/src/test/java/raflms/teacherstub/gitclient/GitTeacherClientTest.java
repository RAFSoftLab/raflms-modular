package raflms.teacherstub.gitclient;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GitTeacherClientTest {

    @Test
    public void testPushAssignment(){
        GitTeacherClient gitTeacherClient = new GitTeacherClient();
        gitTeacherClient.pushAssignment("/home/bojana/raflmsgit/OOP/mojprvitestOOP/grupa1/prvitermin/.git","/home/bojana/Documents/probagitlms/");
    }

}