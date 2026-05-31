package raflms.controller;

import org.springframework.web.bind.annotation.*;
import raflms.dtos.StudentAssignmentResponse;
import raflms.dtos.StudentStartAssignmentRequest;
import raflms.dtos.StudentSubmissionResponse;
import raflms.service.StudentSubmissionService;

import java.util.List;

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

    @GetMapping("/allfortest")
    public List<StudentSubmissionResponse> getAllSubmissionsForTestName(@RequestParam String testName){
        return studentSubimisionService.getStudentSubmissionsForTestName(testName);
    }



}
