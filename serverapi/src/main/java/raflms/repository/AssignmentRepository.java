package raflms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import raflms.model.Assignment;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends ListCrudRepository<Assignment,Long> {


    @Query("select a from Assignment a where a.groupLabel like :group and a.term like :term and a.test.testName like :testName")
    List<Assignment> findAssignemnt(String testName, String group, String term);

    @Query("select a from Assignment a where a.test.testName like :testName")
    List<Assignment> getAssignemntsForTestName(String testName);

    @Query("select a from Assignment a where a.repoPath like :repoPath")
    Assignment getAssignmentForRepoPath(String repoPath);

}
