package raflms.trackingstub.api;

import raflms.trackingstub.config.TrackingStubConfig;
import raflms.trackingstub.dtos.EventBatchDto;
import raflms.trackingstub.dtos.StudentEventDto;
import raflms.trackingstub.restclient.TrackingRestClient;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class TrackingStubService {

    private static final int MAX_BATCH_SIZE = 10;
    private static final int FLUSH_INTERVAL_SECONDS = 30;
    private static final int MAX_RETRIES = 3;

    private final TrackingRestClient restClient;
    private final List<StudentEventDto> pendingEvents = new ArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private String currentSessionId;
    private String currentTaskId = "";

    public TrackingStubService(TrackingStubConfig config) {
        this.restClient = new TrackingRestClient(config.getBaseApiURL());
        startScheduledFlush();
    }

    // -------------------------------------------------------------------------
    // Public API — this is all the plugin ever calls
    // -------------------------------------------------------------------------

    public void startTracking(String studentId, String taskId) {
        this.currentSessionId = UUID.randomUUID().toString();
        this.currentTaskId = taskId;
        logEvent("SESSION_START", studentId, Map.of());
    }

    public void logEvent(String eventType, String studentId) {
        logEvent(eventType, studentId, Map.of());
    }

    public void logEvent(String eventType, String studentId, Map<String, Object> data) {
        StudentEventDto dto = new StudentEventDto(
                studentId,
                currentSessionId,
                eventType,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault()),
                data,
                currentTaskId
        );
        addToPendingBatch(dto);
    }

    public void stopTracking(String studentId) {
        logEvent("SESSION_END", studentId, Map.of());
        flush();
        shutdown();
    }

    // -------------------------------------------------------------------------
    // Batching and transmission — hidden from the plugin
    // -------------------------------------------------------------------------

    private synchronized void addToPendingBatch(StudentEventDto event) {
        pendingEvents.add(event);
        if (pendingEvents.size() >= MAX_BATCH_SIZE) {
            flush();
        }
    }

    private synchronized void flush() {
        if (pendingEvents.isEmpty()) return;

        List<StudentEventDto> batch = new ArrayList<>(pendingEvents);
        pendingEvents.clear();

        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            boolean success = restClient.sendEventBatch(new EventBatchDto(batch));
            if (success) {
                System.out.println("TrackingStub: sent batch of " + batch.size() + " events");
                return;
            }
            System.err.println("TrackingStub: attempt " + (attempt + 1) + " failed, retrying...");
            try {
                Thread.sleep(1000L * (attempt + 1));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        System.err.println("TrackingStub: dropped batch of " + batch.size() + " events after " + MAX_RETRIES + " retries");
    }

    private void startScheduledFlush() {
        scheduler.scheduleAtFixedRate(this::flush,
                FLUSH_INTERVAL_SECONDS, FLUSH_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    private void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}