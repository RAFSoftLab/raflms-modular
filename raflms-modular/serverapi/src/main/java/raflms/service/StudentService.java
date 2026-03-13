package raflms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import raflms.dtos.*;
import raflms.model.StudentInfo;
import raflms.model.StudentScheduledForTest;
import raflms.model.Test;
import raflms.repository.StudentInfoRepository;
import raflms.repository.StudentScheduledForTestRepository;
import raflms.repository.TestRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    private final StudentInfoRepository studentInfoRepo;
    private final TestRepository testRepo;
    private final StudentScheduledForTestRepository studentScheduledForTestRepo;

    public StudentService(StudentInfoRepository studentInfoRepo, TestRepository testRepo, StudentScheduledForTestRepository studentScheduledForTestRepo) {
        this.studentInfoRepo = studentInfoRepo;
        this.testRepo = testRepo;
        this.studentScheduledForTestRepo = studentScheduledForTestRepo;
    }

    /*
            Dodaje studenta ako ne postoji u bazi student sa istim brojem indeksa
            vraca id studenta
             */
    public Long addIfNotExsists(StudentInfoDTO student){
        StudentInfo rez = studentInfoRepo.getStudentInfoForIndex(student.getIndexNumber(), student.getStartYear(), student.getStudyProgramShort());
        if(rez!=null)
            return rez.getId();
        else{
            StudentInfo s = new StudentInfo(student.getFirstName(), student.getLastName(), student.getIndexNumber(), student.getStartYear(), student.getStudyProgramShort());
            s = studentInfoRepo.save(s);
            return s.getId();}
    }

    public List<StudentInfoDTO> getAllStudents(){
        Iterable<StudentInfo> studenti = studentInfoRepo.findAll();
        List<StudentInfoDTO> retVal = new ArrayList<>();
        for(StudentInfo student:studenti){
            StudentInfoDTO sDTO = new StudentInfoDTO(student.getFirstName(), student.getLastName(), student.getIndexNumber(), student.getStartYear(), student.getStudyProgramShort());
            retVal.add(sDTO);
        }
        return retVal;
    }
   // ako student ne postoji u bazi dodaje ga
    public boolean registerStudentForTest(StudentForTestRequest st){
        Test t = testRepo.getTestByName(st.getTestName());
        if(t==null){
            log.error(String.format("Test with a name %s does not exists, student cannnot be registered", st.getTestName()));
            return false;
        }
        StudentInfo si = studentInfoRepo.getStudentInfoForIndex(st.getIndexNumber(), st.getStartYear(),st.getStudyProgramShort());
        if(si==null) {
            log.info(String.format("Dodaje se novi student, index = %s %d/%s ", st.getStudyProgramShort(), st.getIndexNumber(), st.getStartYear()));
            si = new StudentInfo(st.getFirstName(), st.getLastName(), st.getIndexNumber(), st.getStartYear(), st.getStudyProgramShort());
            si = studentInfoRepo.save(si);
        }
        StudentScheduledForTest stNew = new StudentScheduledForTest(si,t);
        studentScheduledForTestRepo.save(stNew);
        return true;
    }

    // ako student ne postoji u bazi dodaje ga
    public boolean registerStudentsForTest(StudentsForTest st){
        Test t = testRepo.getTestByName(st.getTestName());
        if(t==null){
            log.error(String.format("Test with a name %s does not exists, students cannnot be registered", st.getTestName()));
            return false;
        }
        for(StudentInfoDTO s:st.getStudents()) {
            StudentInfo si = studentInfoRepo.getStudentInfoForIndex(s.getIndexNumber(), s.getStartYear(), s.getStudyProgramShort());
            if (si == null) {
                log.info(String.format("Dodaje se novi student, index = %s %d/%s ", s.getStudyProgramShort(), s.getIndexNumber(), s.getStartYear()));
                si = new StudentInfo(s.getFirstName(), s.getLastName(), s.getIndexNumber(), s.getStartYear(), s.getStudyProgramShort());
                si = studentInfoRepo.save(si);
            }
            StudentScheduledForTest stNew = new StudentScheduledForTest(si, t);
            studentScheduledForTestRepo.save(stNew);
        }
        return true;
    }
    


    }
