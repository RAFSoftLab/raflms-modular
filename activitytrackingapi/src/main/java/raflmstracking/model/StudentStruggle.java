package raflmstracking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_struggles")
public class StudentStruggle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false, length = 50)
    private String studentId;

    @Column(name = "session_id", nullable = false, length = 36)
    private String sessionId;

    @Column(name = "struggle_type", nullable = false, length = 50)
    private String struggleType; // 'COMPILATION', 'INACTIVITY', 'CODE_THRASHING', etc.

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "severity_score")
    private Integer severityScore; // 1-10 scale

    @Column(name = "details", columnDefinition = "TEXT")
    private String details; // JSON string with additional info

    // Constructors
    public StudentStruggle() {}

    public StudentStruggle(String studentId, String sessionId, String struggleType,
                           LocalDateTime startTime, Integer severityScore) {
        this.studentId = studentId;
        this.sessionId = sessionId;
        this.struggleType = struggleType;
        this.startTime = startTime;
        this.severityScore = severityScore;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getStruggleType() { return struggleType; }
    public void setStruggleType(String struggleType) { this.struggleType = struggleType; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Integer getSeverityScore() { return severityScore; }
    public void setSeverityScore(Integer severityScore) { this.severityScore = severityScore; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
