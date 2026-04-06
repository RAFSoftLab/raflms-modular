package raflmstracking.controller;

import raflmstracking.dtos.EventBatchDTO;
import raflmstracking.dtos.StudentEventDTO;
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

    /*
    // Get session summary
    @GetMapping("/sessions/student/{studentId}")
    public ResponseEntity<ResponseMessage> getStudentSessions(@PathVariable String studentId) {
        try {
            List<StudentSession> sessions = studentSessionRepository.findByStudentIdOrderByStartTimeDesc(studentId);
            return ResponseEntity.ok(new ResponseMessage(gson.toJson(sessions)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Failed to retrieve sessions: " + e.getMessage()));
        }
    }

    // Get struggles for a student
    @GetMapping("/struggles/student/{studentId}")
    public ResponseEntity<ResponseMessage> getStudentStruggles(@PathVariable String studentId) {
        try {
            List<StudentStruggle> struggles = studentStruggleRepository.findByStudentIdOrderByStartTimeDesc(studentId);
            return ResponseEntity.ok(new ResponseMessage(gson.toJson(struggles)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Failed to retrieve struggles: " + e.getMessage()));
        }
    }

    // Analytics endpoint - high severity struggles
    @GetMapping("/analytics/struggles/high-severity")
    public ResponseEntity<ResponseMessage> getHighSeverityStruggles(@RequestParam(defaultValue = "7") Integer minSeverity) {
        try {
            List<StudentStruggle> struggles = studentStruggleRepository.findHighSeverityStruggles(minSeverity);
            return ResponseEntity.ok(new ResponseMessage(gson.toJson(struggles)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Failed to retrieve high severity struggles: " + e.getMessage()));
        }
    }
*/


}
