package raflms.projectreposervice.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import raflms.conf.RAFLMSProperties;
import raflms.projectreposervice.ProjectRepoService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Primary
public class FileRepoService implements ProjectRepoService {

    private static final Logger log = LoggerFactory.getLogger(FileRepoService.class);
    private final RAFLMSProperties raflmsProperties;

    public FileRepoService(RAFLMSProperties raflmsProperties) {
        this.raflmsProperties = raflmsProperties;
    }

    @Override
    public String createRepo(String subjectShortName, String testName) {
        String path = raflmsProperties.getProjectrootdir() + "/" + subjectShortName + "/" + testName;
        path = path.replaceAll(" ", "");
        try {
            Files.createDirectories(Path.of(path));
        } catch (Exception e) {
             log.error(e.getMessage());
        }
        return path;
    }

    @Override
    public String createRepo(String subjectShortName, String testName, String groupLabel, String term) {

        String path = createRepoPath(subjectShortName, testName, groupLabel, term);
        path = path.replaceAll(" ", "");
        try {
            Files.createDirectories(Path.of(path));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return path;

    }

    @Override
    public String createRepoPath(String subjectShortName, String testName, String groupLabel, String term) {
        String path = raflmsProperties.getProjectrootdir() + "/" + subjectShortName + "/" + testName + "/" + groupLabel + "/" + term;
        path = path.replaceAll(" ", "");
        return path;

    }

    @Override
    public boolean repoExists(String path) {
        return Files.exists(Path.of(path));
    }

    @Override
    public String createStudentRepo(String repoPath, String token) {
        String studentRepoPathStr = repoPath.substring(0,repoPath.lastIndexOf("/"))+ "/studentrepos/" + token;
        Path studentRepoPath = Path.of(studentRepoPathStr);
        // da li kopirati projekat,? simulacija git clone-a

        try {
            Files.createDirectories(studentRepoPath);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return studentRepoPathStr;

    }
}
