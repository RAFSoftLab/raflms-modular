package raflmstracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// @EnableScheduling je neophodno za DataRetentionService (@Scheduled GDPR job)
@SpringBootApplication
@EnableScheduling
public class RAFLMSTrackingServerApp {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RAFLMSTrackingServerApp.class);
        app.run();
    }
}
