package raflmstracking.service;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raflmstracking.dtos.EventBatchDTO;
import raflmstracking.dtos.StudentEventDTO;
import raflmstracking.model.StudentEvent;
import raflmstracking.model.StudentSession;
import raflmstracking.model.StudentStruggle;
import raflmstracking.repository.StudentEventRepository;
import raflmstracking.repository.StudentSessionRepository;
import raflmstracking.repository.StudentStruggleRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentTrackingServiceTest {

    @Mock private StudentEventRepository studentEventRepository;
    @Mock private StudentSessionRepository studentSessionRepository;
    @Mock private StudentStruggleRepository studentStruggleRepository;

    // Use real Gson — it has no side effects and mocking it is noise
    private final Gson gson = new Gson();

    private StudentTrackingService service;

    @BeforeEach
    void setUp() {
        service = new StudentTrackingService(
                studentEventRepository, studentSessionRepository, studentStruggleRepository, gson);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private StudentEventDTO makeDTO(String eventType, String studentId,
                                    String sessionId, LocalDateTime timestamp) {
        StudentEventDTO dto = new StudentEventDTO();
        dto.setEventType(eventType);
        dto.setStudentId(studentId);
        dto.setSessionId(sessionId);
        dto.setTimestamp(timestamp);
        dto.setTaskId("task-1");
        dto.setEventData(Map.of("key", "value"));
        return dto;
    }

    private StudentEvent makeEntity(String eventType, String studentId,
                                    String sessionId, LocalDateTime timestamp) {
        StudentEvent e = new StudentEvent();
        e.setEventType(eventType);
        e.setStudentId(studentId);
        e.setSessionId(sessionId);
        e.setTimestamp(timestamp);
        e.setTaskId("task-1");
        e.setEventData("{}");
        return e;
    }

    // -------------------------------------------------------------------------
    // ingestEventBatch
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("ingestEventBatch")
    class IngestEventBatch {

        @Test
        @DisplayName("returns true and persists all events on success")
        void successPath() {
            LocalDateTime now = LocalDateTime.now();
            EventBatchDTO batch = new EventBatchDTO(List.of(
                    makeDTO("CODE_CHANGE", "stu-1", "ses-1", now),
                    makeDTO("COMPILATION_SUCCESS", "stu-1", "ses-1", now.plusSeconds(5))
            ));

            when(studentSessionRepository.findBySessionId("ses-1"))
                    .thenReturn(Optional.of(new StudentSession("ses-1", "stu-1", "task-1", now)));

            boolean result = service.ingestEventBatch(batch);

            assertThat(result).isTrue();
            verify(studentEventRepository).saveAll(argThat(events ->
                    ((List<StudentEvent>) events).size() == 2));
        }

        @Test
        @DisplayName("returns false when repository throws")
        void repositoryThrows() {
            EventBatchDTO batch = new EventBatchDTO(List.of(
                    makeDTO("CODE_CHANGE", "stu-1", "ses-1", LocalDateTime.now())));

            when(studentEventRepository.saveAll(any())).thenThrow(new RuntimeException("DB down"));

            boolean result = service.ingestEventBatch(batch);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("converts DTO fields to entity correctly")
        void dtoToEntityMapping() {
            LocalDateTime ts = LocalDateTime.of(2025, 6, 1, 10, 0);
            EventBatchDTO batch = new EventBatchDTO(List.of(
                    makeDTO("FOCUS_LOST", "stu-42", "ses-99", ts)));

            when(studentSessionRepository.findBySessionId(anyString()))
                    .thenReturn(Optional.empty());

            service.ingestEventBatch(batch);

            ArgumentCaptor<List<StudentEvent>> captor = ArgumentCaptor.forClass(List.class);
            verify(studentEventRepository).saveAll(captor.capture());

            StudentEvent saved = captor.getValue().get(0);
            assertThat(saved.getStudentId()).isEqualTo("stu-42");
            assertThat(saved.getSessionId()).isEqualTo("ses-99");
            assertThat(saved.getEventType()).isEqualTo("FOCUS_LOST");
            assertThat(saved.getTimestamp()).isEqualTo(ts);
            assertThat(saved.getTaskId()).isEqualTo("task-1");
        }
    }

    // -------------------------------------------------------------------------
    // Session statistics
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("updateSessionStatistics (via ingestEventBatch)")
    class SessionStatistics {

        @Test
        @DisplayName("SESSION_START sets startTime from event timestamp")
        void sessionStartSetsStartTime() {
            LocalDateTime startTime = LocalDateTime.of(2025, 6, 1, 9, 0);
            EventBatchDTO batch = new EventBatchDTO(List.of(
                    makeDTO("SESSION_START", "stu-1", "ses-1", startTime)));

            when(studentSessionRepository.findBySessionId("ses-1"))
                    .thenReturn(Optional.of(new StudentSession("ses-1", "stu-1", "task-1", LocalDateTime.now())));

            service.ingestEventBatch(batch);

            ArgumentCaptor<StudentSession> captor = ArgumentCaptor.forClass(StudentSession.class);
            verify(studentSessionRepository).save(captor.capture());

            assertThat(captor.getValue().getStartTime()).isEqualTo(startTime);
        }

        @Test
        @DisplayName("SESSION_END sets endTime and computes duration")
        void sessionEndSetsDuration() {
            LocalDateTime start = LocalDateTime.of(2025, 6, 1, 9, 0);
            LocalDateTime end   = LocalDateTime.of(2025, 6, 1, 10, 30); // 90 min later

            StudentSession existing = new StudentSession("ses-1", "stu-1", "task-1", start);
            existing.setStartTime(start);

            when(studentSessionRepository.findBySessionId("ses-1")).thenReturn(Optional.of(existing));

            EventBatchDTO batch = new EventBatchDTO(List.of(
                    makeDTO("SESSION_END", "stu-1", "ses-1", end)));

            service.ingestEventBatch(batch);

            ArgumentCaptor<StudentSession> captor = ArgumentCaptor.forClass(StudentSession.class);
            verify(studentSessionRepository).save(captor.capture());

            StudentSession saved = captor.getValue();
            assertThat(saved.getEndTime()).isEqualTo(end);
            assertThat(saved.getSessionDurationMinutes()).isEqualTo(90);
        }

        @Test
        @DisplayName("counters are incremented correctly for mixed event batch")
        void countersIncrementCorrectly() {
            LocalDateTime now = LocalDateTime.now();
            EventBatchDTO batch = new EventBatchDTO(List.of(
                    makeDTO("CODE_CHANGE",          "stu-1", "ses-1", now),
                    makeDTO("CODE_CHANGE",          "stu-1", "ses-1", now.plusSeconds(1)),
                    makeDTO("COMPILATION_ERROR",    "stu-1", "ses-1", now.plusSeconds(2)),
                    makeDTO("COMPILATION_SUCCESS",  "stu-1", "ses-1", now.plusSeconds(3)),
                    makeDTO("FOCUS_LOST",           "stu-1", "ses-1", now.plusSeconds(4))
            ));

            StudentSession existing = new StudentSession("ses-1", "stu-1", "task-1", now);
            when(studentSessionRepository.findBySessionId("ses-1")).thenReturn(Optional.of(existing));

            service.ingestEventBatch(batch);

            ArgumentCaptor<StudentSession> captor = ArgumentCaptor.forClass(StudentSession.class);
            verify(studentSessionRepository).save(captor.capture());

            StudentSession saved = captor.getValue();
            assertThat(saved.getTotalEvents()).isEqualTo(5);
            assertThat(saved.getCodeChanges()).isEqualTo(2);
            assertThat(saved.getCompilationAttempts()).isEqualTo(2); // ERROR + SUCCESS
            assertThat(saved.getSuccessfulCompilations()).isEqualTo(1);
            assertThat(saved.getFocusLostCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("creates a new session when none exists in repository")
        void createsNewSessionWhenMissing() {
            LocalDateTime now = LocalDateTime.now();
            EventBatchDTO batch = new EventBatchDTO(List.of(
                    makeDTO("CODE_CHANGE", "stu-99", "ses-new", now)));

            when(studentSessionRepository.findBySessionId("ses-new")).thenReturn(Optional.empty());

            service.ingestEventBatch(batch);

            ArgumentCaptor<StudentSession> captor = ArgumentCaptor.forClass(StudentSession.class);
            verify(studentSessionRepository).save(captor.capture());

            assertThat(captor.getValue().getStudentId()).isEqualTo("stu-99");
            assertThat(captor.getValue().getSessionId()).isEqualTo("ses-new");
        }
    }

    // -------------------------------------------------------------------------
    // getStudentEventsForId
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("getStudentEventsForId")
    class GetStudentEvents {

        @Test
        @DisplayName("returns mapped DTOs for known student")
        void returnsMappedDTOs() {
            LocalDateTime now = LocalDateTime.now();
            StudentEvent entity = makeEntity("CODE_CHANGE", "stu-1", "ses-1", now);
            entity.setEventData(gson.toJson(Map.of("file", "Main.java")));

            when(studentEventRepository.findByStudentIdOrderByTimestampAsc("stu-1"))
                    .thenReturn(List.of(entity));

            List<StudentEventDTO> result = service.getStudentEventsForId("stu-1");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getEventType()).isEqualTo("CODE_CHANGE");
            assertThat(result.get(0).getStudentId()).isEqualTo("stu-1");
        }

        @Test
        @DisplayName("taskId is correctly mapped from entity (bug-fix regression)")
        void taskIdMappedCorrectly() {
            StudentEvent entity = makeEntity("CODE_CHANGE", "stu-1", "ses-1", LocalDateTime.now());
            entity.setTaskId("task-XYZ");
            entity.setEventData("{}");

            when(studentEventRepository.findByStudentIdOrderByTimestampAsc("stu-1"))
                    .thenReturn(List.of(entity));

            List<StudentEventDTO> result = service.getStudentEventsForId("stu-1");

            // This verifies the dto.setTaskId(event.getTaskId()) fix
            assertThat(result.get(0).getTaskId()).isEqualTo("task-XYZ");
        }

        @Test
        @DisplayName("returns empty list when repository throws")
        void returnsEmptyListOnError() {
            when(studentEventRepository.findByStudentIdOrderByTimestampAsc(any()))
                    .thenThrow(new RuntimeException("DB error"));

            List<StudentEventDTO> result = service.getStudentEventsForId("stu-1");

            assertThat(result).isEmpty();
        }
    }

    // -------------------------------------------------------------------------
    // getStudentSessions / getSession
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Session queries")
    class SessionQueries {

        @Test
        @DisplayName("getStudentSessions returns sessions ordered by start time")
        void getStudentSessions() {
            StudentSession s1 = new StudentSession("ses-1", "stu-1", "task-1", LocalDateTime.now());
            StudentSession s2 = new StudentSession("ses-2", "stu-1", "task-1", LocalDateTime.now().minusHours(1));

            when(studentSessionRepository.findByStudentIdOrderByStartTimeDesc("stu-1"))
                    .thenReturn(List.of(s1, s2));

            List<StudentSession> result = service.getStudentSessions("stu-1");

            assertThat(result).containsExactly(s1, s2);
        }

        @Test
        @DisplayName("getSession returns null when session not found")
        void getSessionNotFound() {
            when(studentSessionRepository.findBySessionId("missing")).thenReturn(Optional.empty());

            assertThat(service.getSession("missing")).isNull();
        }

        @Test
        @DisplayName("getStudentSessions returns empty list on repository error")
        void sessionQueryError() {
            when(studentSessionRepository.findByStudentIdOrderByStartTimeDesc(any()))
                    .thenThrow(new RuntimeException("DB error"));

            assertThat(service.getStudentSessions("stu-1")).isEmpty();
        }
    }

    // -------------------------------------------------------------------------
    // Struggle queries
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("Struggle queries")
    class StruggleQueries {

        @Test
        @DisplayName("getStudentStruggles delegates to repository")
        void getStudentStruggles() {
            StudentStruggle struggle = new StudentStruggle(
                    "stu-1", "ses-1", "COMPILATION", LocalDateTime.now(), 8);

            when(studentStruggleRepository.findByStudentIdOrderByStartTimeDesc("stu-1"))
                    .thenReturn(List.of(struggle));

            List<StudentStruggle> result = service.getStudentStruggles("stu-1");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStruggleType()).isEqualTo("COMPILATION");
        }

        @Test
        @DisplayName("getHighSeverityStruggles delegates threshold to repository")
        void getHighSeverityStruggles() {
            when(studentStruggleRepository.findHighSeverityStruggles(7)).thenReturn(List.of());

            service.getHighSeverityStruggles(7);

            verify(studentStruggleRepository).findHighSeverityStruggles(7);
        }

        @Test
        @DisplayName("getSessionStruggles returns empty list on error")
        void sessionStruggleError() {
            when(studentStruggleRepository.findBySessionIdOrderByStartTimeAsc(any()))
                    .thenThrow(new RuntimeException("DB error"));

            assertThat(service.getSessionStruggles("ses-1")).isEmpty();
        }
    }
}