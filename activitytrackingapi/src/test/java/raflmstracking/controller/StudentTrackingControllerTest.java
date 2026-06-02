package raflmstracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raflmstracking.dtos.EventBatchDTO;
import raflmstracking.dtos.StudentEventDTO;
import raflmstracking.model.StudentSession;
import raflmstracking.model.StudentStruggle;
import raflmstracking.service.StudentTrackingService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentTrackingController.class)
class StudentTrackingControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean  StudentTrackingService trackingService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private StudentEventDTO sampleEventDTO() {
        StudentEventDTO dto = new StudentEventDTO();
        dto.setStudentId("stu-1");
        dto.setSessionId("ses-1");
        dto.setEventType("CODE_CHANGE");
        dto.setTimestamp(LocalDateTime.of(2025, 6, 1, 10, 0));
        dto.setTaskId("task-1");
        dto.setEventData(Map.of("file", "Main.java"));
        return dto;
    }

    private EventBatchDTO sampleBatch() {
        return new EventBatchDTO(List.of(sampleEventDTO()));
    }

    // -------------------------------------------------------------------------
    // POST /tracking/events/batch
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("POST /tracking/events/batch")
    class IngestBatch {

        @Test
        @DisplayName("returns true when service ingests successfully")
        void returns200OnSuccess() throws Exception {
            when(trackingService.ingestEventBatch(any(EventBatchDTO.class))).thenReturn(true);

            mockMvc.perform(post("/tracking/events/batch")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(sampleBatch())))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));
        }

        @Test
        @DisplayName("returns false when service signals failure")
        void returnsFalseOnServiceFailure() throws Exception {
            when(trackingService.ingestEventBatch(any(EventBatchDTO.class))).thenReturn(false);

            mockMvc.perform(post("/tracking/events/batch")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(sampleBatch())))
                    .andExpect(status().isOk())
                    .andExpect(content().string("false"));
        }

        @Test
        @DisplayName("returns 400 on malformed JSON body")
        void returns400OnBadJson() throws Exception {
            mockMvc.perform(post("/tracking/events/batch")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("not-json"))
                    .andExpect(status().isBadRequest());
        }
    }

    // -------------------------------------------------------------------------
    // GET /tracking/events/student/{studentId}
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("GET /tracking/events/student/{studentId}")
    class GetStudentEvents {

        @Test
        @DisplayName("returns list of events for known student")
        void returnsEventList() throws Exception {
            when(trackingService.getStudentEventsForId("stu-1"))
                    .thenReturn(List.of(sampleEventDTO()));

            mockMvc.perform(get("/tracking/events/student/stu-1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].studentId").value("stu-1"))
                    .andExpect(jsonPath("$[0].eventType").value("CODE_CHANGE"))
                    .andExpect(jsonPath("$[0].taskId").value("task-1"));
        }

        @Test
        @DisplayName("returns empty array when student has no events")
        void returnsEmptyArray() throws Exception {
            when(trackingService.getStudentEventsForId("stu-unknown")).thenReturn(List.of());

            mockMvc.perform(get("/tracking/events/student/stu-unknown"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }

    // -------------------------------------------------------------------------
    // GET /tracking/events/session/{sessionId}
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("GET /tracking/events/session/{sessionId}")
    class GetSessionEvents {

        @Test
        @DisplayName("returns events for a given session")
        void returnsEvents() throws Exception {
            when(trackingService.getSessionEvents("ses-1"))
                    .thenReturn(List.of(sampleEventDTO()));

            mockMvc.perform(get("/tracking/events/session/ses-1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].sessionId").value("ses-1"));
        }
    }

    // -------------------------------------------------------------------------
    // GET /tracking/sessions/student/{studentId}
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("GET /tracking/sessions/student/{studentId}")
    class GetStudentSessions {

        @Test
        @DisplayName("returns session list for student")
        void returnsSessions() throws Exception {
            StudentSession session = new StudentSession(
                    "ses-1", "stu-1", "task-1",
                    LocalDateTime.of(2025, 6, 1, 9, 0));

            when(trackingService.getStudentSessions("stu-1")).thenReturn(List.of(session));

            mockMvc.perform(get("/tracking/sessions/student/stu-1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].sessionId").value("ses-1"))
                    .andExpect(jsonPath("$[0].studentId").value("stu-1"));
        }
    }

    // -------------------------------------------------------------------------
    // GET /tracking/sessions/{sessionId}
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("GET /tracking/sessions/{sessionId}")
    class GetSession {

        @Test
        @DisplayName("returns session when found")
        void returnsSession() throws Exception {
            StudentSession session = new StudentSession(
                    "ses-1", "stu-1", "task-1",
                    LocalDateTime.of(2025, 6, 1, 9, 0));

            when(trackingService.getSession("ses-1")).thenReturn(session);

            mockMvc.perform(get("/tracking/sessions/ses-1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.sessionId").value("ses-1"));
        }

        @Test
        @DisplayName("returns empty body when session not found")
        void returnsNullBody() throws Exception {
            when(trackingService.getSession("missing")).thenReturn(null);

            mockMvc.perform(get("/tracking/sessions/missing"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(""));
        }
    }

    // -------------------------------------------------------------------------
    // GET /tracking/struggles/student/{studentId}
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("GET /tracking/struggles/student/{studentId}")
    class GetStudentStruggles {

        @Test
        @DisplayName("returns struggles for student")
        void returnsStruggles() throws Exception {
            StudentStruggle struggle = new StudentStruggle(
                    "stu-1", "ses-1", "COMPILATION",
                    LocalDateTime.of(2025, 6, 1, 9, 30), 8);

            when(trackingService.getStudentStruggles("stu-1")).thenReturn(List.of(struggle));

            mockMvc.perform(get("/tracking/struggles/student/stu-1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].struggleType").value("COMPILATION"))
                    .andExpect(jsonPath("$[0].severityScore").value(8));
        }
    }

    // -------------------------------------------------------------------------
    // GET /tracking/struggles/high-severity
    // -------------------------------------------------------------------------

    @Nested
    @DisplayName("GET /tracking/struggles/high-severity")
    class GetHighSeverityStruggles {

        @Test
        @DisplayName("uses default minSeverity of 7 when not specified")
        void usesDefaultThreshold() throws Exception {
            when(trackingService.getHighSeverityStruggles(7)).thenReturn(List.of());

            mockMvc.perform(get("/tracking/struggles/high-severity"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("passes custom minSeverity from query param")
        void passesCustomThreshold() throws Exception {
            when(trackingService.getHighSeverityStruggles(9)).thenReturn(List.of());

            mockMvc.perform(get("/tracking/struggles/high-severity?minSeverity=9"))
                    .andExpect(status().isOk());
        }
    }
}