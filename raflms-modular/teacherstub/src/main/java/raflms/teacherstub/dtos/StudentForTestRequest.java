package raflms.teacherstub.dtos;

public class StudentForTestRequest {

    private String firstName;
    private String lastName;
    private Integer indexNumber;
    private String startYear;
    private String studyProgramShort;

    private String testName;

    public StudentForTestRequest(String firstName, String lastName, Integer indexNumber, String startYear, String studyProgramShort, String testName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
        this.startYear = startYear;
        this.studyProgramShort = studyProgramShort;
        this.testName = testName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(Integer indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getStudyProgramShort() {
        return studyProgramShort;
    }

    public void setStudyProgramShort(String studyProgramShort) {
        this.studyProgramShort = studyProgramShort;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
}
