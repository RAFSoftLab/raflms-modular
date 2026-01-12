package raflms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RAFLMSServerApp {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RAFLMSServerApp.class);
        app.run();

    }
}
