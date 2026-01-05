package raflms.dtos;

public class StudentAssignementResponse {

    private String gitForkPath;
    private String token;

    public StudentAssignementResponse() {
    }

    public StudentAssignementResponse(String gitForkPath, String token) {
        this.gitForkPath = gitForkPath;
        this.token = token;
    }

    public String getGitForkPath() {
        return gitForkPath;
    }

    public void setGitForkPath(String gitForkPath) {
        this.gitForkPath = gitForkPath;
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
                "gitForkPath='" + gitForkPath + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
