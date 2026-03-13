package raflms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import raflms.model.Test;

public interface TestRepository extends ListCrudRepository<Test,Long> {

    @Query("select t from Test t where t.testName like :testName")
    Test getTestByName(String testName);
}
