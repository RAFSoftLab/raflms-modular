package raflms.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import raflms.dtos.AssignmentRequest;
import raflms.dtos.AssignmentResponse;
import raflms.dtos.TestDTO;
import raflms.service.TestService;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    private TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    // dodaje test ili menja postojeci ako postoji sa istim imenom
    @PostMapping(path="/add")
    public Boolean addTest(@RequestBody TestDTO test){
        return testService.addTest(test);
    }

    @GetMapping(path="/all")
    public List<TestDTO> getAllTests(){
        return testService.getAllTests();
    }

    @GetMapping(path="/find")
    public TestDTO findByName(@RequestParam String testName){
        return testService.findByName(testName);
    }


    @PostMapping(path="/addassignment")
    public AssignmentResponse addAssignment(@RequestBody AssignmentRequest assignment){
        log.info("Adding assignment to server and creating empty repo");
        return testService.addAssignment(assignment);

    }
}
