package raflms.studentstub.dtos;

public class StudentAssignmentResponse {

    private String studentFolderPath;
    private String assignmentPath;
    private String token;

    public StudentAssignmentResponse() {
    }

    public StudentAssignmentResponse(String studentFolderPath, String assignmentPath, String token) {
        this.studentFolderPath = studentFolderPath;
        this.assignmentPath = assignmentPath;
        this.token = token;
    }

    public String getStudentFolderPath() {
        return studentFolderPath;
    }

    public void setStudentFolderPath(String studentFolderPath) {
        this.studentFolderPath = studentFolderPath;
    }

    public String getAssignmentPath() {
        return assignmentPath;
    }

    public void setAssignmentPath(String assignmentPath) {
        this.assignmentPath = assignmentPath;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "StudentAssignmentResponse{" +
                "studentFolderPath='" + studentFolderPath + '\'' +
                ", assignmentPath='" + assignmentPath + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
