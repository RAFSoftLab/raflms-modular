package raflms.trackingstub.config;

public class TrackingStubConfig {

    private String baseApiURL;

    public TrackingStubConfig(String baseApiURL) {
        this.baseApiURL = baseApiURL;
    }

    public String getBaseApiURL() {
        return baseApiURL;
    }

    public void setBaseApiURL(String baseApiURL) {
        this.baseApiURL = baseApiURL;
    }
}