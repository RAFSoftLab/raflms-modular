package raflmstracking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raflmstracking.model.StudentSession;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentSessionRepository extends JpaRepository<StudentSession, String> {

    List<StudentSession> findByStudentIdOrderByStartTimeDesc(String studentId);

    Optional<StudentSession> findBySessionId(String sessionId);

    List<StudentSession> findByTaskIdOrderByStartTimeDesc(String taskId);

    // RISK-14 (GDPR): brise sesije ciji je startTime stariji od zadatog datuma
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("DELETE FROM StudentSession s WHERE s.startTime < :cutoff")
    int deleteByStartTimeBefore(@org.springframework.data.repository.query.Param("cutoff") java.time.LocalDateTime cutoff);
}
