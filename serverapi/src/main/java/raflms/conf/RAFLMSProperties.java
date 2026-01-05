package raflms.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("raflmsProperties")
@ConfigurationProperties("raflms")
public class RAFLMSProperties {

    private String gitrootdir;

    public String getGitrootdir() {
        return gitrootdir;
    }

    public void setGitrootdir(String gitrootdir) {
        this.gitrootdir = gitrootdir;
    }
}
