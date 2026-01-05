package raflms.teacherstub.config;

public class TeacherStubConfig {

    private String baseApiURL;

    private String localGitRootDir;

    public TeacherStubConfig(String baseApiURL, String localGitRootDir) {
        this.baseApiURL = baseApiURL;
        this.localGitRootDir = localGitRootDir;
    }

    public String getBaseApiURL() {
        return baseApiURL;
    }

    public void setBaseApiURL(String baseApiURL) {
        this.baseApiURL = baseApiURL;
    }

    public String getLocalGitRootDir() {
        return localGitRootDir;
    }

    public void setLocalGitRootDir(String localGitRootDir) {
        this.localGitRootDir = localGitRootDir;
    }
}
