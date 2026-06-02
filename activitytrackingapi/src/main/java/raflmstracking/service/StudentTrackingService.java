package raflmstracking.service;

import com.google.gson.Gson;
import raflmstracking.dtos.EventBatchDTO;
import raflmstracking.dtos.StudentEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import raflmstracking.model.StudentEvent;
import raflmstracking.model.StudentSession;
import raflmstracking.model.StudentStruggle;
import raflmstracking.repository.StudentEventRepository;
import raflmstracking.repository.StudentSessionRepository;
import raflmstracking.repository.StudentStruggleRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentTrackingService {

    private final StudentEventRepository studentEventRepository;
    private final StudentSessionRepository studentSessionRepository;
    private final StudentStruggleRepository studentStruggleRepository;
    private final Gson gson;

    private static final Logger log = LoggerFactory.getLogger(StudentTrackingService.class);

    public StudentTrackingService(StudentEventRepository studentEventRepository,
                                  StudentSessionRepository studentSessionRepository,
                                  StudentStruggleRepository studentStruggleRepository,
                                  Gson gson) {
        this.studentEventRepository = studentEventRepository;
        this.studentSessionRepository = studentSessionRepository;
        this.studentStruggleRepository = studentStruggleRepository;
        this.gson = gson;
    }

    // -------------------------------------------------------------------------
    // Ingestion
    // -------------------------------------------------------------------------

    public boolean ingestEventBatch(EventBatchDTO eventDTO) {
        try {
            List<StudentEvent> events = eventDTO.getEvents().stream()
                    .map(this::convertToEntity)
                    .collect(Collectors.toList());

            studentEventRepository.saveAll(events);
            updateSessionStatistics(events);
            log.info("Successfully ingested {} events", events.size());
            return true;
        } catch (Exception e) {
            log.error("Failed to ingest event batch: {}", e.getMessage());
            return false;
        }
    }

    // -------------------------------------------------------------------------
    // Event queries
    // -------------------------------------------------------------------------

    public List<StudentEventDTO> getStudentEventsForId(String studentId) {
        try {
            List<StudentEvent> events = studentEventRepository.findByStudentIdOrderByTimestampAsc(studentId);
            log.info("Retrieved {} events for student {}", events.size(), studentId);
            return events.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to retrieve events for student {}: {}", studentId, e.getMessage());
            return List.of();
        }
    }

    public List<StudentEventDTO> getSessionEvents(String sessionId) {
        try {
            List<StudentEvent> events = studentEventRepository.findBySessionIdOrderByTimestampAsc(sessionId);
            log.info("Retrieved {} events for session {}", events.size(), sessionId);
            return events.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to retrieve events for session {}: {}", sessionId, e.getMessage());
            return List.of();
        }
    }

    // -------------------------------------------------------------------------
    // Session queries
    // -------------------------------------------------------------------------

    public List<StudentSession> getStudentSessions(String studentId) {
        try {
            List<StudentSession> sessions = studentSessionRepository.findByStudentIdOrderByStartTimeDesc(studentId);
            log.info("Retrieved {} sessions for student {}", sessions.size(), studentId);
            return sessions;
        } catch (Exception e) {
            log.error("Failed to retrieve sessions for student {}: {}", studentId, e.getMessage());
            return List.of();
        }
    }

    public StudentSession getSession(String sessionId) {
        try {
            return studentSessionRepository.findBySessionId(sessionId).orElse(null);
        } catch (Exception e) {
            log.error("Failed to retrieve session {}: {}", sessionId, e.getMessage());
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // Struggle queries
    // -------------------------------------------------------------------------

    public List<StudentStruggle> getStudentStruggles(String studentId) {
        try {
            List<StudentStruggle> struggles = studentStruggleRepository.findByStudentIdOrderByStartTimeDesc(studentId);
            log.info("Retrieved {} struggles for student {}", struggles.size(), studentId);
            return struggles;
        } catch (Exception e) {
            log.error("Failed to retrieve struggles for student {}: {}", studentId, e.getMessage());
            return List.of();
        }
    }

    public List<StudentStruggle> getSessionStruggles(String sessionId) {
        try {
            List<StudentStruggle> struggles = studentStruggleRepository.findBySessionIdOrderByStartTimeAsc(sessionId);
            log.info("Retrieved {} struggles for session {}", struggles.size(), sessionId);
            return struggles;
        } catch (Exception e) {
            log.error("Failed to retrieve struggles for session {}: {}", sessionId, e.getMessage());
            return List.of();
        }
    }

    public List<StudentStruggle> getHighSeverityStruggles(Integer minSeverity) {
        try {
            return studentStruggleRepository.findHighSeverityStruggles(minSeverity);
        } catch (Exception e) {
            log.error("Failed to retrieve high-severity struggles: {}", e.getMessage());
            return List.of();
        }
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private void updateSessionStatistics(List<StudentEvent> events) {
        events.stream()
                .collect(Collectors.groupingBy(StudentEvent::getSessionId))
                .forEach((sessionId, sessionEvents) -> {

                    StudentSession session = studentSessionRepository.findBySessionId(sessionId)
                            .orElse(new StudentSession(
                                    sessionId,
                                    sessionEvents.get(0).getStudentId(),
                                    sessionEvents.get(0).getTaskId(),
                                    LocalDateTime.now()));

                    // Handle SESSION_START — set the actual start time from event timestamp
                    sessionEvents.stream()
                            .filter(e -> "SESSION_START".equals(e.getEventType()))
                            .findFirst()
                            .ifPresent(e -> session.setStartTime(e.getTimestamp()));

                    // Handle SESSION_END — set end time and compute duration
                    sessionEvents.stream()
                            .filter(e -> "SESSION_END".equals(e.getEventType()))
                            .findFirst()
                            .ifPresent(e -> {
                                session.setEndTime(e.getTimestamp());
                                if (session.getStartTime() != null) {
                                    long minutes = ChronoUnit.MINUTES.between(
                                            session.getStartTime(), e.getTimestamp());
                                    session.setSessionDurationMinutes((int) minutes);
                                }
                            });

                    // Update counters
                    session.setTotalEvents(session.getTotalEvents() + sessionEvents.size());

                    long compilationAttempts = sessionEvents.stream()
                            .filter(e -> e.getEventType().contains("COMPILATION"))
                            .count();
                    session.setCompilationAttempts(
                            session.getCompilationAttempts() + (int) compilationAttempts);

                    long successfulCompilations = sessionEvents.stream()
                            .filter(e -> "COMPILATION_SUCCESS".equals(e.getEventType()))
                            .count();
                    session.setSuccessfulCompilations(
                            session.getSuccessfulCompilations() + (int) successfulCompilations);

                    long codeChanges = sessionEvents.stream()
                            .filter(e -> "CODE_CHANGE".equals(e.getEventType()))
                            .count();
                    session.setCodeChanges(session.getCodeChanges() + (int) codeChanges);

                    long focusLost = sessionEvents.stream()
                            .filter(e -> "FOCUS_LOST".equals(e.getEventType()))
                            .count();
                    session.setFocusLostCount(session.getFocusLostCount() + (int) focusLost);

                    studentSessionRepository.save(session);
                });
    }

    private StudentEvent convertToEntity(StudentEventDTO dto) {
        StudentEvent event = new StudentEvent();
        event.setStudentId(dto.getStudentId());
        event.setSessionId(dto.getSessionId());
        event.setEventType(dto.getEventType());
        event.setTimestamp(dto.getTimestamp());
        event.setEventData(gson.toJson(dto.getEventData()));
        event.setTaskId(dto.getTaskId());
        return event;
    }

    private StudentEventDTO convertToDTO(StudentEvent event) {
        StudentEventDTO dto = new StudentEventDTO();
        dto.setStudentId(event.getStudentId());
        dto.setSessionId(event.getSessionId());
        dto.setEventType(event.getEventType());
        dto.setTimestamp(event.getTimestamp());
        dto.setEventData(gson.fromJson(event.getEventData(), Map.class));
        dto.setTaskId(event.getTaskId()); // BUG FIX: was dto.getTaskId()
        return dto;
    }
}