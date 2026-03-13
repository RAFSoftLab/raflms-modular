package raflms.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import raflms.model.StudentInfo;

public interface StudentInfoRepository extends CrudRepository<StudentInfo,String> {

    @Query("select s from StudentInfo s where s.indexNumber = :indexNumber and " +
            "s.startYear like :startYear and s.studyProgramShort like :studyProgramShort")
    StudentInfo getStudentInfoForIndex(Integer indexNumber, String startYear, String studyProgramShort);





}
