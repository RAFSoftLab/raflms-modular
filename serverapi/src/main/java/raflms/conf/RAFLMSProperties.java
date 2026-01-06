package raflms.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("raflmsProperties")
@ConfigurationProperties("raflms")
public class RAFLMSProperties {

    private String gitrootdir;

    private String projectrootdir;

    public String getGitrootdir() {
        return gitrootdir;
    }

    public void setGitrootdir(String gitrootdir) {
        this.gitrootdir = gitrootdir;
    }

    public String getProjectrootdir() {
        return projectrootdir;
    }

    public void setProjectrootdir(String projectrootdir) {
        this.projectrootdir = projectrootdir;
    }
}
