package raflms.projectreposervice;

import java.time.LocalDateTime;

public interface ProjectRepoService {

    String createRepo(String subjectShortName, String testName);

    String createRepo(String subjectShortName, String testName, String groupLabel, String term);


    String createRepoPath(String subjectShortName, String testName, String groupLabel, String term);

    boolean repoExists(String path);

    public String createStudentRepo(String repoPath, String token);

    public String createStudentRepo(String repoPath, int indexNumber, String startYear, String studyProgramShortName, String studentGroup);
}
