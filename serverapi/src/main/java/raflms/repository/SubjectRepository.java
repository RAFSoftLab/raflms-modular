package raflms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import raflms.model.Subject;

public interface SubjectRepository extends ListCrudRepository<Subject,Long> {

    @Query("select s from Subject s where s.shortName like :shortName")
    Subject getSubjectForShortName(String shortName);

}