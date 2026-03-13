package raflms.teacherstub.dtos;



public class AssignmentRequest {

    private String groupLabel;

    private String term;

    private String testName;

    private Long testId;


    public AssignmentRequest() {
    }

    public AssignmentRequest(String groupLabel, String term, String testName) {
        this.groupLabel = groupLabel;
        this.term = term;
        this.testName = testName;
    }

    public AssignmentRequest(String groupLabel, String term, Long testId) {
        this.groupLabel = groupLabel;
        this.term = term;
        this.testId = testId;
    }

    public String getGroupLabel() {
        return groupLabel;
    }

    public void setGroupLabel(String groupLabel) {
        this.groupLabel = groupLabel;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }


}
