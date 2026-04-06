package raflmstracking.service;

import com.google.gson.Gson;
import raflmstracking.dtos.EventBatchDTO;
import raflmstracking.dtos.StudentEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import raflmstracking.model.StudentEvent;
import raflmstracking.model.StudentSession;
import raflmstracking.repository.StudentEventRepository;
import raflmstracking.repository.StudentSessionRepository;
import raflmstracking.repository.StudentStruggleRepository;

import java.time.LocalDateTime;
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

    public StudentTrackingService(StudentEventRepository studentEventRepository, StudentSessionRepository studentSessionRepository, StudentStruggleRepository studentStruggleRepository, Gson gson) {
        this.studentEventRepository = studentEventRepository;
        this.studentSessionRepository = studentSessionRepository;
        this.studentStruggleRepository = studentStruggleRepository;
        this.gson = gson;
    }

    public boolean ingestEventBatch(EventBatchDTO eventDTO){
        try {
            List<StudentEvent> events = eventDTO.getEvents().stream()
                    .map(this::convertToEntity)
                    .collect(Collectors.toList());

            studentEventRepository.saveAll(events);
            // Update session statistics
            updateSessionStatistics(events);
            log.info(String.format("Successfully ingested %d events", events.size()));
            return true;
        } catch (Exception e) {
            log.error("Failed to ingest event batch: " + e.getMessage());
            return false;
        }
    }

    public List<StudentEventDTO> getStudentEventsForId(String studentId){
        try {
            List<StudentEvent> events = studentEventRepository.findByStudentIdOrderByTimestampAsc(studentId);
            List<StudentEventDTO> retVal = events.stream().map(this::convertToDTO).toList();
            log.info("Retrieved events for student with id "+studentId);
            return retVal;
        } catch (Exception e) {
            log.error("Failed to retrieve events for student with id "+studentId);
            return null;
        }
    }

    public List<StudentEventDTO> getSessionEvents(String sessionId){
        try {
            List<StudentEvent> events = studentEventRepository.findBySessionIdOrderByTimestampAsc(sessionId);
            List<StudentEventDTO> retVal = events.stream().map(this::convertToDTO).toList();
            log.info("Student events retrieved for sessionId = " + sessionId);
            return retVal;
        }catch(Exception e){
            log.error("Failed to retrieve events for session with id "+sessionId);
            return null;
        }
    }

    // Helper methods
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

    // Helper methods
    private StudentEventDTO convertToDTO(StudentEvent event) {
        StudentEventDTO dto = new StudentEventDTO();
        dto.setStudentId(event.getStudentId());
        dto.setSessionId(event.getSessionId());
        dto.setEventType(event.getEventType());
        dto.setTimestamp(event.getTimestamp());
        dto.setEventData(gson.fromJson(event.getEventData(), Map.class));
        dto.setTaskId(dto.getTaskId());
        return dto;
    }

    private void updateSessionStatistics(List<StudentEvent> events) {
        // Group events by session and update statistics
        events.stream()
                .collect(Collectors.groupingBy(StudentEvent::getSessionId))
                .forEach((sessionId, sessionEvents) -> {
                    StudentSession session = studentSessionRepository.findBySessionId(sessionId)
                            .orElse(new StudentSession(sessionId,
                                    sessionEvents.get(0).getStudentId(),
                                    sessionEvents.get(0).getTaskId(),
                                    LocalDateTime.now()));

                    // Update counters
                    session.setTotalEvents(session.getTotalEvents() + sessionEvents.size());

                    long compilationEvents = sessionEvents.stream()
                            .filter(e -> e.getEventType().contains("COMPILATION"))
                            .count();
                    session.setCompilationAttempts(session.getCompilationAttempts() + (int) compilationEvents);

                    long codeChangeEvents = sessionEvents.stream()
                            .filter(e -> e.getEventType().equals("CODE_CHANGE"))
                            .count();
                    session.setCodeChanges(session.getCodeChanges() + (int) codeChangeEvents);

                    studentSessionRepository.save(session);
                });
    }

}
