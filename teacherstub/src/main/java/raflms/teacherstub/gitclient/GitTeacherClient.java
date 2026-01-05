package raflms.teacherstub.gitclient;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.URIish;
import raflms.teacherstub.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class GitTeacherClient {

    private final String localGitRepoPath;

    public GitTeacherClient(String localGitRepoPath) {
        this.localGitRepoPath = localGitRepoPath;
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

/*
public boolean pushAssignmentCloneRepoFromServer(String remoteRepoPath, String assignmentProjectDir){
        try {

            FileUtils.deleteDirectoryIfExists(localGitRepoPath);
            File f = new File(localGitRepoPath);
            Git git = Git.cloneRepository()
                    .setURI(remoteRepoPath)
                    .setDirectory(f)
                    .call();
            FileUtils.copyFolder(Path.of(assignmentProjectDir), Path.of(localGitRepoPath));
            git.add().addFilepattern(".").call();
            git.commit().setMessage("poruka").call();
            PushCommand pushCommand = git.push();
            pushCommand.call();
            git.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }
 */
