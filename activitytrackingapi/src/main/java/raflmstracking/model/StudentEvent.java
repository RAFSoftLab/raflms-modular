package raflmstracking.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_events")
public class StudentEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false, length = 50)
    private String studentId;

    @Column(name = "session_id", nullable = false, length = 36)
    private String sessionId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "event_data", columnDefinition = "TEXT")
    private String eventData; // JSON string

    @Column(name = "task_id", length = 50)
    private String taskId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors
    public StudentEvent() {
        this.createdAt = LocalDateTime.now();
    }

    public StudentEvent(String studentId, String sessionId, String eventType,
                        LocalDateTime timestamp, String eventData, String taskId) {
        this.studentId = studentId;
        this.sessionId = sessionId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.eventData = eventData;
        this.taskId = taskId;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getEventData() { return eventData; }
    public void setEventData(String eventData) { this.eventData = eventData; }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
