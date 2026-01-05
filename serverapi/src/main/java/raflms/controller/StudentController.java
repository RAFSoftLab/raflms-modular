package raflms.controller;


import org.springframework.web.bind.annotation.*;
import raflms.authorisation.TokenManager;
import raflms.dtos.*;
import raflms.service.StudentService;
import raflms.service.StudentSubmissionService;


import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final StudentSubmissionService studentSubimiisonService;


    public StudentController(StudentService studentService, StudentSubmissionService studentSubimiisonService) {
        this.studentService = studentService;
        this.studentSubimiisonService = studentSubimiisonService;
    }

    @PostMapping(path="/add")
    public Long addNewStudent (@RequestBody StudentInfoDTO student) {
        return studentService.addIfNotExsists(student);
    }

    @GetMapping(path="/all")
    public List<StudentInfoDTO> getAllStudents(){
        return studentService.getAllStudents();

    }

    @PostMapping(path="/registerfortest")
    public boolean registerStudentForTest(@RequestBody StudentForTestRequest st){
        return studentService.registerStudentForTest(st);

    }

    @PostMapping(path="/registerallfortest")
    public boolean registerStudentForTest(@RequestBody StudentsForTest st){
        return studentService.registerStudentsForTest(st);

    }


    @PostMapping("/authorizeforasigment")
    public StudentAssignementResponse authorizeStudentForAssignemnt(@PathVariable StudentStartAssignmentRequest sa) {
       return studentSubimiisonService.studentStartingAssigment(sa);
    }








}
