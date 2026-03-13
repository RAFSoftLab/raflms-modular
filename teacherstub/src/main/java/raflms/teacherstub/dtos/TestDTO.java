package raflms.teacherstub.dtos;

import java.time.LocalDate;

public class TestDTO {

    private String testName; // neformalno ime da bude jedinstveno

    private LocalDate testDate;

    private String subjectShortName;

    private String testType;

    public TestDTO() {
    }

    public TestDTO(String testName, LocalDate testDate, String subjectShortName, String testType) {
        this.testName = testName;
        this.testDate = testDate;
        this.subjectShortName = subjectShortName;
        this.testType = testType;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public LocalDate getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDate testDate) {
        this.testDate = testDate;
    }

    public String getSubjectShortName() {
        return subjectShortName;
    }

    public void setSubjectShortName(String subjectShortName) {
        this.subjectShortName = subjectShortName;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }
}
