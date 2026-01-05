package raflms.controller;

import org.springframework.web.bind.annotation.*;
import raflms.dtos.StudentAssignmentResponse;
import raflms.dtos.StudentStartAssignmentRequest;
import raflms.service.StudentSubmissionService;

@RestController
@RequestMapping("/student/submission")
public class StudentSubmissinsController {

    private final StudentSubmissionService studentSubimiisonService;

    public StudentSubmissinsController(StudentSubmissionService studentSubimiisonService) {
        this.studentSubimiisonService = studentSubimiisonService;
    }

    @PostMapping("/authorizeforasignment")
    public StudentAssignmentResponse authorizeStudentForAssignemnt(@RequestBody StudentStartAssignmentRequest sa) {
        return studentSubimiisonService.studentStartingAssigment(sa);
    }

    @PostMapping("/submitassigment")
    public StudentAssignmentResponse submitAssigment() {
        return null;
    }



}
