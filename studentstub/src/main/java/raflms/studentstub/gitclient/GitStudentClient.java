package raflms.studentstub.gitclient;

import org.eclipse.jgit.api.Git;


import java.io.File;

public class GitStudentClient {


    public boolean cloneAssigmentRepo(String assigmentRepoPath, String projectRoot){
        try {

            File f = new File(projectRoot);
            Git git = Git.cloneRepository()
                    .setURI(assigmentRepoPath)
                    .setDirectory(f)
                    .call();
            git.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);

        }

    }


}
