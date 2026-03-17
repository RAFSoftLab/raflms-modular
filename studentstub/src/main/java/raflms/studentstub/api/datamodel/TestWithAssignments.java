package raflms.studentstub.api.datamodel;

import java.util.List;

public class TestWithAssignments {
    private String testName;
    private List<AssignmentData> assigments;

    public TestWithAssignments(String testName) {
        this.testName = testName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public List<AssignmentData> getAssigments() {
        return assigments;
    }

    public void setAssigments(List<AssignmentData> assigments) {
        this.assigments = assigments;
    }

    @Override
    public String toString() {
        return "TestWithAssignments{" +
                "testName='" + testName + '\'' +
                ", assigments=" + assigments +
                '}';
    }
}
