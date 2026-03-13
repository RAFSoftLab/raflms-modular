package raflms.service;

import org.springframework.stereotype.Service;
import raflms.dtos.SubjectDTO;
import raflms.model.Subject;
import raflms.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectService {

    private SubjectRepository subjectRepo;

    public SubjectService(SubjectRepository subjectRepo) {
        this.subjectRepo = subjectRepo;
    }

    public List<SubjectDTO> getAllSubjects() {
        List<Subject> subjects = subjectRepo.findAll();
        return subjects.stream().map(s->new SubjectDTO(s.getFullName(),s.getShortName())).toList();
    }

    public Long addIfNotExists(SubjectDTO subject){
        Subject s = subjectRepo.getSubjectForShortName(subject.getShortName());
        if(s!=null)
            return s.getId();
        else{
            Subject sNew = new Subject(subject.getFullName(), subject.getShortName());
            sNew = subjectRepo.save(sNew);
            return sNew.getId();
        }
    }
}
