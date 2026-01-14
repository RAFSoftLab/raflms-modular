package raflms.controller;

import org.springframework.web.bind.annotation.*;
import raflms.dtos.StudentAssignmentResponse;
import raflms.dtos.StudentStartAssignmentRequest;
import raflms.service.StudentSubmissionService;

@RestController
@RequestMapping("/student/submission")
public class StudentSubmissinsController {

    private final StudentSubmissionService studentSubimisionService;

    public StudentSubmissinsController(StudentSubmissionService studentSubimisionService) {
        this.studentSubimisionService = studentSubimisionService;
    }

    @PostMapping("/authorizeforasignment")
    public StudentAssignmentResponse authorizeStudentForAssignemnt(@RequestBody StudentStartAssignmentRequest sa) {
        return studentSubimisionService.studentStartingAssigment(sa);
    }

    @PostMapping("/submitassigment")
    public StudentAssignmentResponse submitAssigment() {
        return null;
    }



}
