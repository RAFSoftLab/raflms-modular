package raflms.projectreposervice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import raflms.projectreposervice.impl.GitRepoService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GitRepoServiceTestIT {

    @Autowired
    private ProjectRepoService gitRepoService;



    @Test
    public void testCreateGitRepo(){
        String branch = gitRepoService.createRepo("OOP","mytest");
        assertNotNull(branch);
        System.out.println(branch);
    }

    @Test
    public void testCreateGitRepoWithAssigmentDetails(){
        String branch = gitRepoService.createRepo("OOP","mytest","grupa1","prviTermin");
        assertNotNull(branch);
        System.out.println(branch);
    }

}