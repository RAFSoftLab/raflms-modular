package raflms.trackingstub.api;

import org.junit.jupiter.api.Test;
import raflms.trackingstub.config.ConfigFactory;

import java.util.Map;

class TrackingStubServiceTest {

    private final TrackingStubService trackingService =
            new TrackingStubService(ConfigFactory.createConfig());

    @Test
    public void testStartAndStopTracking() {
        trackingService.startTracking("stu-1", "task-test-1");
        System.out.println("Tracking started");

        trackingService.logEvent("CODE_CHANGE", "stu-1", Map.of("file", "Main.java", "line", 5));
        trackingService.logEvent("FOCUS_LOST", "stu-1");
        trackingService.logEvent("COMPILATION_STARTED", "stu-1");

        trackingService.stopTracking("stu-1");
        System.out.println("Tracking stopped");
    }

    @Test
    public void testLogMultipleEventTypes() {
        trackingService.startTracking("stu-2", "task-test-2");

        trackingService.logEvent("CODE_CHANGE", "stu-2", Map.of("file", "Solution.java"));
        trackingService.logEvent("COMPILATION_STARTED", "stu-2");
        trackingService.logEvent("COMPILATION_FINISHED", "stu-2", Map.of("success", true));
        trackingService.logEvent("FOCUS_LOST", "stu-2");
        trackingService.logEvent("FOCUS_GAINED", "stu-2");
        trackingService.logEvent("KEYBOARD_ACTIVITY", "stu-2");

        trackingService.stopTracking("stu-2");
        System.out.println("All event types logged successfully");
    }
}