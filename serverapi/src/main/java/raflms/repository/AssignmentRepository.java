package raflms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import raflms.model.Assignment;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends ListCrudRepository<Assignment,Long> {


    // RISK-20 fix: = umesto like sprecava da % kao unos vrati sve zapise
    @Query("select a from Assignment a where a.groupLabel = :group and a.term = :term and a.test.testName = :testName")
    List<Assignment> findAssignemnt(String testName, String group, String term);

    @Query("select a from Assignment a where a.test.testName = :testName")
    List<Assignment> getAssignemntsForTestName(String testName);

    @Query("select a from Assignment a where a.repoPath = :repoPath")
    Assignment getAssignmentForRepoPath(String repoPath);

}
