package raflms.projectreposervice.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import raflms.conf.RAFLMSProperties;
import raflms.model.StudentInfo;
import raflms.projectreposervice.ProjectRepoService;
import raflms.repository.StudentInfoRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
@Primary
public class FileRepoService implements ProjectRepoService {

    private static final Logger log = LoggerFactory.getLogger(FileRepoService.class);
    private final RAFLMSProperties raflmsProperties;
    private final StudentInfoRepository studentRepo;

    public FileRepoService(RAFLMSProperties raflmsProperties, StudentInfoRepository studRepo) {
        this.raflmsProperties = raflmsProperties;
        this.studentRepo = studRepo;
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

        try {
            Files.createDirectories(studentRepoPath);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return studentRepoPathStr;

    }

    @Override
    public String createStudentRepo(String repoPath, int indexNumber, String startYear, String studyProgramShortName, String studentGroup) {
        StudentInfo s = studentRepo.getStudentInfoForIndex(indexNumber, startYear, studyProgramShortName);
        String studentRepoName = String.format("%s-%s-%s-%s-%d-%s-%s", studentGroup,s.getFirstName(),s.getLastName(), studyProgramShortName, indexNumber, startYear, LocalDateTime.now().toString());
        String path = repoPath.substring(0,repoPath.lastIndexOf("/"))+ "/studentrepos/" + studentRepoName;
        //path = path.replaceAll(" ", "");
        return path;
    }


}
