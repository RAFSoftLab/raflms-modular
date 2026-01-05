package raflms.controller;


import org.springframework.web.bind.annotation.*;
import raflms.dtos.SubjectDTO;
import raflms.service.SubjectService;

import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    private SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping(path="/all")
    public List<SubjectDTO> getAllSubjects(){
        return subjectService.getAllSubjects();
    }

    @PostMapping(path="/add")
    public Long addSubject(@RequestBody SubjectDTO subject){
        return subjectService.addIfNotExists(subject);

    }
}
