package raflms.gitservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GitRepoServiceTestIT {

    @Autowired
    private GitRepoService gitRepoService;



    @Test
    public void testCreateGitRepo(){
        String branch = gitRepoService.createGitRepo("OOP","mytest");
        assertNotNull(branch);
        System.out.println(branch);
    }

    @Test
    public void testCreateGitRepoWithAssigmentDetails(){
        String branch = gitRepoService.createGitRepo("OOP","mytest","grupa1","prviTermin");
        assertNotNull(branch);
        System.out.println(branch);
    }

}