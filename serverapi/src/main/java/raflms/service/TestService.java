package raflms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import raflms.dtos.AssignmentRequest;
import raflms.dtos.AssignmentResponse;
import raflms.dtos.TestDTO;
import raflms.gitservice.GitRepoService;
import raflms.model.Assignment;
import raflms.model.Subject;
import raflms.model.Test;
import raflms.repository.AssignmentRepository;
import raflms.repository.SubjectRepository;
import raflms.repository.TestRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    private static final Logger log = LoggerFactory.getLogger(TestService.class);
    private final TestRepository testRepo;
    private final AssignmentRepository assignmentRepo;
    private final SubjectRepository subjectRepo;
    private final GitRepoService gitRepoService;

    public TestService(TestRepository testRepo, AssignmentRepository assignmentRepo, SubjectRepository subjectRepo, GitRepoService gitRepoService) {
        this.testRepo = testRepo;
        this.assignmentRepo = assignmentRepo;
        this.subjectRepo = subjectRepo;
        this.gitRepoService = gitRepoService;
    }

    public Boolean addTest(TestDTO testDTO){
        Subject sub = subjectRepo.getSubjectForShortName(testDTO.getSubjectShortName());
        if(sub==null){
            log.error(String.format("Adding test, subject with short name %s does not exists, cannot add test",testDTO.getSubjectShortName()));
            return false;
        }else{
            Test t = testRepo.getTestByName(testDTO.getTestName());
            if(t==null) {
                t = new Test(testDTO.getTestName(), testDTO.getTestDate(), sub, testDTO.getTestType());
                t = testRepo.save(t);
            }else{
                t.setTestDate(testDTO.getTestDate());
                t.setTestType(testDTO.getTestType());
                testRepo.save(t);
            }
            return true;
        }
    }

    public List<TestDTO> getAllTests(){
        List<Test> tests = testRepo.findAll();
        return tests.stream().map(t->new TestDTO(t.getTestName(),t.getTestDate(),t.getSubject().getShortName(),t.getTestType())).toList();
    }

    public TestDTO findByName(String testName){
        Test t = testRepo.getTestByName(testName);
        if(t==null)
            return null;
        else
            return new TestDTO(t.getTestName(),t.getTestDate(),t.getSubject().getShortName(),t.getTestType());
    }

    public AssignmentResponse addAssignment(AssignmentRequest a){
        Test t = null;
        if(a.getTestId()!=null && a.getTestId()!=0){
            Optional<Test> rez = testRepo.findById(a.getTestId());
            if(rez.isPresent())
                t = rez.get();
        }
        if(t==null) {
            t = testRepo.getTestByName(a.getTestName());
        }
        if(t==null){
            log.error("Trying to add assignment, there is no test for the given id or test name");
            return null;
        }
        Assignment ass = new Assignment(a.getTerm(), a.getGroupLabel(),t);
        String gitRepoPath = gitRepoService.createGitRepo(t.getSubject().getShortName(), a.getTestName(), a.getGroupLabel(), a.getTerm());
        if(gitRepoPath==null){
            log.error("Trying to add assignment, cannot create rit repo");
        }
        ass.setGitRepoPath(gitRepoPath);
        Assignment as = assignmentRepo.save(ass);
        AssignmentResponse assRes = new AssignmentResponse(as.getId(),as.getGroupLabel(), as.getTerm(), as.getTest().getTestName(), as.getTest().getId(), as.getTest().getSubject().getShortName(), as.getGitRepoPath());
        return assRes;
    }



}
