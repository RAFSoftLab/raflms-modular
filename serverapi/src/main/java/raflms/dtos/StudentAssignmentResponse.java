package raflms.dtos;

public class StudentAssignmentResponse {

    private String studentRepoPath;
    private String token;

    public StudentAssignmentResponse() {
    }

    public StudentAssignmentResponse(String studentRepoPath, String token) {
        this.studentRepoPath = studentRepoPath;
        this.token = token;
    }

    public String getStudentRepoPath() {
        return studentRepoPath;
    }

    public void setStudentRepoPath(String studentRepoPath) {
        this.studentRepoPath = studentRepoPath;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "StudentAssignementResponse{" +
                "gitForkPath='" + studentRepoPath + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
