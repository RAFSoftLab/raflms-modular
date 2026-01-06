package raflms.studentstub.repoclient.impl;

import org.eclipse.jgit.api.Git;
import raflms.studentstub.repoclient.StudentRepoClient;


import java.io.File;

public class GitStudentClient implements StudentRepoClient {


    public boolean retrieveAssignmentProject(String assigmentRepoPath, String projectRoot){
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

    @Override
    public boolean submitProject(String studentRepoPath, String projectRoot) {
        return false;
    }


}
