package raflmstracking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_sessions")
public class StudentSession {

    @Id
    @Column(name = "session_id", length = 36)
    private String sessionId;

    @Column(name = "student_id", nullable = false, length = 50)
    private String studentId;

    @Column(name = "task_id", length = 50)
    private String taskId;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "total_events")
    private Integer totalEvents = 0;

    @Column(name = "compilation_attempts")
    private Integer compilationAttempts = 0;

    @Column(name = "successful_compilations")
    private Integer successfulCompilations = 0;

    @Column(name = "code_changes")
    private Integer codeChanges = 0;

    @Column(name = "focus_lost_count")
    private Integer focusLostCount = 0;

    @Column(name = "session_duration_minutes")
    private Integer sessionDurationMinutes = 0;

    // Constructors
    public StudentSession() {}

    public StudentSession(String sessionId, String studentId, String taskId, LocalDateTime startTime) {
        this.sessionId = sessionId;
        this.studentId = studentId;
        this.taskId = taskId;
        this.startTime = startTime;
    }

    // Getters and Setters
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Integer getTotalEvents() { return totalEvents; }
    public void setTotalEvents(Integer totalEvents) { this.totalEvents = totalEvents; }

    public Integer getCompilationAttempts() { return compilationAttempts; }
    public void setCompilationAttempts(Integer compilationAttempts) { this.compilationAttempts = compilationAttempts; }

    public Integer getSuccessfulCompilations() { return successfulCompilations; }
    public void setSuccessfulCompilations(Integer successfulCompilations) { this.successfulCompilations = successfulCompilations; }

    public Integer getCodeChanges() { return codeChanges; }
    public void setCodeChanges(Integer codeChanges) { this.codeChanges = codeChanges; }

    public Integer getFocusLostCount() { return focusLostCount; }
    public void setFocusLostCount(Integer focusLostCount) { this.focusLostCount = focusLostCount; }

    public Integer getSessionDurationMinutes() { return sessionDurationMinutes; }
    public void setSessionDurationMinutes(Integer sessionDurationMinutes) { this.sessionDurationMinutes = sessionDurationMinutes; }
}
