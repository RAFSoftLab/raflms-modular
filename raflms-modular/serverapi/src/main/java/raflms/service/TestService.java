package raflms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import raflms.dtos.AssignmentRequest;
import raflms.dtos.AssignmentResponse;
import raflms.dtos.TestDTO;
import raflms.projectreposervice.ProjectRepoService;
import raflms.model.Assignment;
import raflms.model.Subject;
import raflms.model.Test;
import raflms.repository.AssignmentRepository;
import raflms.repository.SubjectRepository;
import raflms.repository.TestRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    private static final Logger log = LoggerFactory.getLogger(TestService.class);
    private final TestRepository testRepo;
    private final AssignmentRepository assignmentRepo;
    private final SubjectRepository subjectRepo;
    private final ProjectRepoService projectRepoService;

    public TestService(TestRepository testRepo, AssignmentRepository assignmentRepo, SubjectRepository subjectRepo, ProjectRepoService projectRepoService) {
        this.testRepo = testRepo;
        this.assignmentRepo = assignmentRepo;
        this.subjectRepo = subjectRepo;
        this.projectRepoService = projectRepoService;
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
        List<Assignment> existingAss = assignmentRepo.findAssignemnt(a.getTestName(), a.getGroupLabel(),a.getTerm());
        Assignment ass = null;
        if(!existingAss.isEmpty())
            ass = existingAss.get(0);
        else {
            ass = new Assignment(a.getTerm(), a.getGroupLabel(), t);
        }
        String repoPath = projectRepoService.createRepo(t.getSubject().getShortName(), a.getTestName(), a.getGroupLabel(), a.getTerm());
        if(repoPath==null){
            log.error("Trying to add assignment, cannot create repo");
        }
        ass.setRepoPath(repoPath);
        Assignment as = assignmentRepo.save(ass);
        AssignmentResponse assRes = new AssignmentResponse(as.getId(),as.getGroupLabel(), as.getTerm(), as.getTest().getTestName(), as.getTest().getId(), as.getTest().getSubject().getShortName(), as.getRepoPath());
        return assRes;
    }

    public List<AssignmentResponse> getAssignementsForTestName(String testName){
        List<Assignment> ass = assignmentRepo.getAssignemntsForTestName(testName);
        if(ass==null || ass.isEmpty())
            return List.of();
        else{
            List<AssignmentResponse> retVal = ass.stream().map(a->new AssignmentResponse(a.getId(), a.getGroupLabel(),a.getTerm(),a.getTest().getTestName()
                    ,a.getTest().getId(), a.getTest().getSubject().getShortName(),a.getRepoPath())).toList();
            return retVal;
        }
    }

    public void updateRepoPath(String oldRepoPath, String newRepoPath){
        Assignment as = assignmentRepo.getAssignmentForRepoPath(oldRepoPath);
        as.setRepoPath(newRepoPath);
        assignmentRepo.save(as);
    }





}
