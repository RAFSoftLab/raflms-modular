package raflms.teacherstub.projectrepoclient.impl;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.transport.URIish;
import raflms.teacherstub.projectrepoclient.TeacherRepoClient;

import java.io.File;

public class GitRepoTeacherClient implements TeacherRepoClient {



    public GitRepoTeacherClient() {

    }

    public boolean pushAssignment(String remoteRepoPath, String assignmentProjectDir){
        try {

            Git git = Git.init().setDirectory(new File(assignmentProjectDir)).call();
            git.add().addFilepattern(".").call();
            git.remoteAdd()
                    .setName("origin")
                    .setUri(new URIish(remoteRepoPath))
                    .call();
            git.commit().setMessage("poruka").call();
            PushCommand pushCommand = git.push();
            pushCommand.call();
            git.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }
}


