package raflms.trackingstub.restclient;

import org.junit.jupiter.api.Test;
import raflms.trackingstub.config.ConfigFactory;
import raflms.trackingstub.dtos.EventBatchDto;
import raflms.trackingstub.dtos.StudentEventDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

class TrackingRestClientTest {

    private final TrackingRestClient restClient =
            new TrackingRestClient(ConfigFactory.createConfig().getBaseApiURL());

    @Test
    public void sendEventBatch() {
        StudentEventDto event = new StudentEventDto(
                "stu-1",
                "ses-test-001",
                "CODE_CHANGE",
                LocalDateTime.now(),
                Map.of("file", "Main.java", "line", 10),
                "task-test-1"
        );

        boolean ok = restClient.sendEventBatch(new EventBatchDto(List.of(event)));
        System.out.println("sendEventBatch result: " + ok);
    }

    @Test
    public void sendMultipleEvents() {
        List<StudentEventDto> events = List.of(
                new StudentEventDto("stu-1", "ses-test-002", "SESSION_START",
                        LocalDateTime.now(), Map.of(), "task-test-1"),
                new StudentEventDto("stu-1", "ses-test-002", "CODE_CHANGE",
                        LocalDateTime.now(), Map.of("file", "Main.java"), "task-test-1"),
                new StudentEventDto("stu-1", "ses-test-002", "COMPILATION_STARTED",
                        LocalDateTime.now(), Map.of(), "task-test-1"),
                new StudentEventDto("stu-1", "ses-test-002", "SESSION_END",
                        LocalDateTime.now(), Map.of(), "task-test-1")
        );

        boolean ok = restClient.sendEventBatch(new EventBatchDto(events));
        System.out.println("sendMultipleEvents result: " + ok);
    }
}