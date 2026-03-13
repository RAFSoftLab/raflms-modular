package raflms.teacherstub.dtos;

public class AssignmentResponse {

    private Long assignmentId;

    private String groupLabel;

    private String term;

    private String testName;

    private Long testId;

    private String subjectShortName;

    private String gitRepoPath;

    public AssignmentResponse() {
    }

    public AssignmentResponse(Long assignmentId, String groupLabel, String term, String testName, Long testId, String subjectShortName, String gitRepoPath) {
        this.assignmentId = assignmentId;
        this.groupLabel = groupLabel;
        this.term = term;
        this.testName = testName;
        this.testId = testId;
        this.subjectShortName = subjectShortName;
        this.gitRepoPath = gitRepoPath;
    }

    public String getGroupLabel() {
        return groupLabel;
    }

    public void setGroupLabel(String groupLabel) {
        this.groupLabel = groupLabel;
    }



    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getSubjectShortName() {
        return subjectShortName;
    }

    public void setSubjectShortName(String subjectShortName) {
        this.subjectShortName = subjectShortName;
    }

    public String getGitRepoPath() {
        return gitRepoPath;
    }

    public void setGitRepoPath(String gitRepoPath) {
        this.gitRepoPath = gitRepoPath;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }
}
