package raflms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import raflms.authorisation.TokenManager;
import raflms.dtos.StudentAssignmentResponse;
import raflms.dtos.StudentStartAssignmentRequest;
import raflms.projectreposervice.ProjectRepoService;
import raflms.projectreposervice.impl.GitRepoService;
import raflms.model.Assignment;
import raflms.model.StudentInfo;
import raflms.model.StudentSubmission;
import raflms.repository.AssignmentRepository;
import raflms.repository.StudentInfoRepository;
import raflms.repository.StudentSubmissionRepository;

import java.util.List;

@Service
public class StudentSubmissionService {

    private static final Logger log = LoggerFactory.getLogger(StudentSubmissionService.class);



    private final StudentInfoRepository studentInfoRepo;
    private final AssignmentRepository assigmnetRepo;
    private final ProjectRepoService projectRepoService;
    private final TokenManager tokenManager;
    private final StudentSubmissionRepository studSubmissionRepo;

    public StudentSubmissionService(StudentInfoRepository studentInfoRepo, AssignmentRepository assigmnetRepo, ProjectRepoService projectRepoService, TokenManager tokenManager, StudentSubmissionRepository studSubmissionRepo) {
        this.studentInfoRepo = studentInfoRepo;
        this.assigmnetRepo = assigmnetRepo;
        this.projectRepoService = projectRepoService;
        this.tokenManager = tokenManager;
        this.studSubmissionRepo = studSubmissionRepo;
    }

    // ako student ne postoji, dodajemo ga, u krajnjoj verziji treba uvesti provere da ne moze da radi ako nije unet
    public StudentAssignmentResponse studentStartingAssigment(StudentStartAssignmentRequest ssa){
        StudentInfo si = studentInfoRepo.getStudentInfoForIndex(ssa.getIndexNumber(),ssa.getStartYear(),ssa.getStudyProgramShortName());
        if(si==null){
            log.warn(String.format("Student cannot be found, adding student with index = %s %d/%s",ssa.getStudyProgramShortName(), ssa.getIndexNumber(),ssa.getStartYear()));
            si = new StudentInfo(ssa.getIndexNumber(),ssa.getStartYear(),ssa.getStudyProgramShortName());
            si = studentInfoRepo.save(si);
        }
        List<Assignment> ass = assigmnetRepo.findAssignemnt(ssa.getTestName(),ssa.getGroup(),ssa.getTerm());
        if(ass.size()>1)
            log.warn(String.format("More than one assignment for testName =%s, group=%s, term=%s",ssa.getTestName(),ssa.getGroup(),ssa.getTerm()));
        if(ass.isEmpty()) {
            log.error(String.format("No assignment found for testName =%s, group=%s, term=%s", ssa.getTestName(), ssa.getGroup(), ssa.getTerm()));
            return null;
        }
        Assignment as = ass.get(0);
        if(!projectRepoService.repoExists(as.getRepoPath())){
            log.error(String.format("No repo found for assignment testName =%s, group=%s, term=%s, gitRepoPath = %s", ssa.getTestName(), ssa.getGroup(), ssa.getTerm(), as.getRepoPath()));
            return null;
        }
        String token = tokenManager.generateToken();
        String studentRepoPath = projectRepoService.createStudentRepo(as.getRepoPath(),token);

        //String studentRepoPath = projectRepoService.createStudentRepo(as.getRepoPath(), ssa.getIndexNumber(), ssa.getStartYear(), ssa.getStudyProgramShortName(), ssa.getStudentGroup());
         StudentSubmission ss = new StudentSubmission(si,as,studentRepoPath,ssa.getStudentGroup(),token);
        //StudentSubmission ss = new StudentSubmission(si,as,studentRepoPath,ssa.getStudentGroup());
        ss = studSubmissionRepo.save(ss);
        StudentAssignmentResponse res = new StudentAssignmentResponse(studentRepoPath,as.getRepoPath());
        return res;
    }




}
