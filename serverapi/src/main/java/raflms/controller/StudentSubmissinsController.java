package raflms.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student/submission")
public class StudentSubmissinsController {

    @PostMapping(path="/start")
    public boolean startAssigment(){
        return true;
    }


}
