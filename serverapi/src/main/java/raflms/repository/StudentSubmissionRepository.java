package raflms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import raflms.model.StudentSubmission;

import java.util.List;
import java.util.Optional;

public interface StudentSubmissionRepository extends ListCrudRepository<StudentSubmission, Long> {

    // RISK-20 fix: = umesto like
    @Query("select s from StudentSubmission s where s.repoPath = :repoPath")
    StudentSubmission getStudentSubmissinForRepoPath(String repoPath);

    @Query("select s from StudentSubmission s where s.assignment.test.testName = :testName")
    List<StudentSubmission> getSubmissionsForTestName(String testName);

    // RISK-08 fix: lookup po UUID-u za download endpoint
    Optional<StudentSubmission> findByPublicId(String publicId);
}
