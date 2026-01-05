package raflms.gitservice;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.util.FS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.stereotype.Service;
import raflms.conf.RAFLMSProperties;
import raflms.model.Assignment;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;


@Service
public class GitRepoService {

    private static final Logger log = LoggerFactory.getLogger(GitRepoService.class);
    private final RAFLMSProperties raflmsProperties;

    public GitRepoService(RAFLMSProperties raflmsProperties) {
        this.raflmsProperties = raflmsProperties;
    }

    /**
     * @return repo url
     */
    public String createGitRepo(String subjectShortName, String testName) {
        try {
            String path = raflmsProperties.getGitrootdir() + "/" + subjectShortName + "/" + testName;
            path = path.replaceAll(" ", "");
            Git git = Git.init().setDirectory(new File(path)).call();
            Repository repo = git.getRepository();
            return repo.getDirectory().getAbsolutePath();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String createGitRepo(String subjectShortName, String testName, String groupLabel, String term) {
        try {

            String path = createGitRepoPath(subjectShortName, testName, groupLabel, term);
            Git git = Git.init().setDirectory(new File(path)).call();
            Repository repo = git.getRepository();
            return repo.getDirectory().getAbsolutePath();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String createGitRepoPath(String subjectShortName, String testName, String groupLabel, String term) {
        String path = raflmsProperties.getGitrootdir() + "/" + subjectShortName + "/" + testName + "/" + groupLabel + "/" + term;
        path = path.replaceAll(" ", "");
        return path;
    }

    public boolean getRepoExists(String path) {
        File f = new File(path);
        return RepositoryCache.FileKey.isGitRepository(f, FS.DETECTED);
    }

    public String createStudentRepo(String gitRepo, String token) {
        String studentRepoPath = gitRepo.replaceAll("\\.git", "") + "studentrepos/" + token;
        File f = new File(studentRepoPath);

        try{

            Git.cloneRepository()
                .setURI(gitRepo)
                .setDirectory(f)
                .call();
        } catch (InvalidRemoteException e) {
            throw new RuntimeException(e);
        } catch (TransportException e) {
            throw new RuntimeException(e);
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
        return studentRepoPath;
    }
}