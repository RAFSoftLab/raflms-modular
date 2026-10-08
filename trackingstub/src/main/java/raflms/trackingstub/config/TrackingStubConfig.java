package raflms.trackingstub.config;

public class TrackingStubConfig {

    private String baseApiURL;
    private String authToken;

    public TrackingStubConfig(String baseApiURL, String authToken) {
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