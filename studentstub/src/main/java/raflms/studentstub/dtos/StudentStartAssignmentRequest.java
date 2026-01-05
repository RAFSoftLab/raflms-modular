package raflms.studentstub.dtos;

public class StudentStartAssignmentRequest {

    // for student
    private int indexNumber;
    private String startYear;
    private String studyProgramShortName;
    private String studentGroup;

    // for assignemnt
    private String testName;
    private String group;
    private String term;

    public StudentStartAssignmentRequest() {
    }

    public StudentStartAssignmentRequest(int indexNumber, String startYear, String studyProgramShortName, String studentGroup, String testName, String group, String term) {
        this.indexNumber = indexNumber;
        this.startYear = startYear;
        this.studyProgramShortName = studyProgramShortName;
        this.studentGroup = studentGroup;
        this.testName = testName;
        this.group = group;
        this.term = term;
    }

    public int getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(int indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getStudyProgramShortName() {
        return studyProgramShortName;
    }

    public void setStudyProgramShortName(String studyProgramShortName) {
        this.studyProgramShortName = studyProgramShortName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(String studentGroup) {
        this.studentGroup = studentGroup;
    }
}
