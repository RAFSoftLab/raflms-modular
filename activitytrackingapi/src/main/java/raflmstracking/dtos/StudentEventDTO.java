package raflmstracking.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Map;

// RISK-18 fix: @NotNull/@NotBlank/@Size sprecavaju upis null/praznih/predugackih vrednosti u bazu
public class StudentEventDTO {

    @NotBlank
    @Size(max = 64)
    private String studentId;

    @NotBlank
    @Size(max = 64)
    private String sessionId;

    @NotBlank
    @Size(max = 64)
    private String eventType;

    @NotNull
    private LocalDateTime timestamp;

    private Map<String, Object> eventData;

    @Size(max = 128)
    private String taskId;

    public StudentEventDTO() {}

    public StudentEventDTO(String studentId, String sessionId, String eventType,
                           LocalDateTime timestamp, Map<String, Object> eventData, String taskId) {
        this.studentId = studentId;
        this.sessionId = sessionId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.eventData = eventData;
        this.taskId = taskId;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Map<String, Object> getEventData() { return eventData; }
    public void setEventData(Map<String, Object> eventData) { this.eventData = eventData; }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
}
