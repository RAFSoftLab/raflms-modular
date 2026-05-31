package raflms.dtos;

import java.time.LocalDateTime;

public class StudentSubmissionResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private Integer indexNumber;
    private String startYear;
    private String studyProgramShort;
    private String studentGroup;
    // submission data
    private LocalDateTime taskStartedTime;
    private boolean taskSubmitted;
    private LocalDateTime taskSubmittedTime;
    // assignment data
    private String testName;
    private String term;
    private String groupLabel;

    public StudentSubmissionResponse(Long id, String firstName, String lastName, Integer indexNumber, String startYear, String studyProgramShort) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
        this.startYear = startYear;
        this.studyProgramShort = studyProgramShort;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(String studentGroup) {
        this.studentGroup = studentGroup;
    }

    public LocalDateTime getTaskStartedTime() {
        return taskStartedTime;
    }

    public void setTaskStartedTime(LocalDateTime taskStartedTime) {
        this.taskStartedTime = taskStartedTime;
    }

    public boolean isTaskSubmitted() {
        return taskSubmitted;
    }

    public void setTaskSubmitted(boolean taskSubmitted) {
        this.taskSubmitted = taskSubmitted;
    }

    public LocalDateTime getTaskSubmittedTime() {
        return taskSubmittedTime;
    }

    public void setTaskSubmittedTime(LocalDateTime taskSubmittedTime) {
        this.taskSubmittedTime = taskSubmittedTime;
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

    public String getGroupLabel() {
        return groupLabel;
    }

    public void setGroupLabel(String groupLabel) {
        this.groupLabel = groupLabel;
    }
}
