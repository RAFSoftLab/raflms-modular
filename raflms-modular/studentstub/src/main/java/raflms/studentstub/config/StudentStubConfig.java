package raflms.studentstub.config;

public class StudentStubConfig {

    private String baseApiURL;

    public StudentStubConfig(String baseApiURL) {
        this.baseApiURL = baseApiURL;
    }

    public String getBaseApiURL() {
        return baseApiURL;
    }

    public void setBaseApiURL(String baseApiURL) {
        this.baseApiURL = baseApiURL;
    }
}
