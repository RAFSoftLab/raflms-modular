package raflms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import raflms.model.StudentSubmission;

import java.util.List;


public interface StudentSubmissionRepository extends ListCrudRepository<StudentSubmission,Long> {

    @Query("select s from StudentSubmission s where s.repoPath like :repoPath")
    StudentSubmission getStudentSubmissinForRepoPath(String repoPath);

    @Query("select s from StudentSubmission s where s.assignment.test.testName like :testName")
    List<StudentSubmission> getSubmissionsForTestName(String testName);



}
