package raflms.repository;

import org.springframework.data.repository.ListCrudRepository;
import raflms.model.StudentScheduledForTest;

public interface StudentScheduledForTestRepository extends ListCrudRepository<StudentScheduledForTest,Long> {
}
