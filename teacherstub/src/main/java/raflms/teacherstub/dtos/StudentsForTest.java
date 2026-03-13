package raflms.teacherstub.dtos;

import java.util.List;

public class StudentsForTest {

    private String testName;

    private List<StudentInfoDTO> students;

    public StudentsForTest() {
    }

    public StudentsForTest(String testName) {
        this.testName = testName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public List<StudentInfoDTO> getStudents() {
        return students;
    }

    public void setStudents(List<StudentInfoDTO> students) {
        this.students = students;
    }
}
