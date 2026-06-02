package raflmstracking.controller;

import raflmstracking.dtos.EventBatchDTO;
import raflmstracking.dtos.StudentEventDTO;
import raflmstracking.model.StudentSession;
import raflmstracking.model.StudentStruggle;
import org.springframework.web.bind.annotation.*;
import raflmstracking.service.StudentTrackingService;

import java.util.List;

@RestController
@RequestMapping("/tracking")
public class StudentTrackingController {

    private final StudentTrackingService studentTrackingService;

    public StudentTrackingController(StudentTrackingService studentTrackingService) {
        this.studentTrackingService = studentTrackingService;
    }

    // Batch event ingestion endpoint
    @PostMapping("/events/batch")
    public Boolean ingestEventBatch(@RequestBody EventBatchDTO eventBatch) {
        return studentTrackingService.ingestEventBatch(eventBatch);
    }

    // Get events for a specific student
    @GetMapping("/events/student/{studentId}")
    public List<StudentEventDTO> getStudentEvents(@PathVariable String studentId) {
        return studentTrackingService.getStudentEventsForId(studentId);
    }

    // Get events for a specific session
    @GetMapping("/events/session/{sessionId}")
    public List<StudentEventDTO> getSessionEvents(@PathVariable String sessionId) {
        return studentTrackingService.getSessionEvents(sessionId);
    }

    // Get session summary for a student
    @GetMapping("/sessions/student/{studentId}")
    public List<StudentSession> getStudentSessions(@PathVariable String studentId) {
        return studentTrackingService.getStudentSessions(studentId);
    }

    // Get a specific session by sessionId
    @GetMapping("/sessions/{sessionId}")
    public StudentSession getSession(@PathVariable String sessionId) {
        return studentTrackingService.getSession(sessionId);
    }

    // Get struggles for a student
    @GetMapping("/struggles/student/{studentId}")
    public List<StudentStruggle> getStudentStruggles(@PathVariable String studentId) {
        return studentTrackingService.getStudentStruggles(studentId);
    }

    // Get struggles for a specific session
    @GetMapping("/struggles/session/{sessionId}")
    public List<StudentStruggle> getSessionStruggles(@PathVariable String sessionId) {
        return studentTrackingService.getSessionStruggles(sessionId);
    }

    // Get high-severity struggles (severity >= threshold)
    @GetMapping("/struggles/high-severity")
    public List<StudentStruggle> getHighSeverityStruggles(
            @RequestParam(defaultValue = "7") Integer minSeverity) {
        return studentTrackingService.getHighSeverityStruggles(minSeverity);
    }
}