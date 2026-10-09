package raflms.studentstub.config;

public class StudentStubConfig {

    private String baseApiURL;
    private String authToken;

    public StudentStubConfig(String baseApiURL, String authToken) {
        this.baseApiURL = baseApiURL;
        this.authToken = authToken;
    }

    public String getBaseApiURL() {
        return baseApiURL;
    }

    public void setBaseApiURL(String baseApiURL) {
        this.baseApiURL = baseApiURL;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
