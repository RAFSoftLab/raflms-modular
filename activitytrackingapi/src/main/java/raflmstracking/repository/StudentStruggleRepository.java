package raflmstracking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raflmstracking.model.StudentStruggle;

import java.util.List;

@Repository
public interface StudentStruggleRepository extends JpaRepository<StudentStruggle, Long> {

    List<StudentStruggle> findByStudentIdOrderByStartTimeDesc(String studentId);

    List<StudentStruggle> findBySessionIdOrderByStartTimeAsc(String sessionId);

    List<StudentStruggle> findByStruggleTypeOrderByStartTimeDesc(String struggleType);

    @Query("SELECT s FROM StudentStruggle s WHERE s.severityScore >= :minSeverity ORDER BY s.severityScore DESC, s.startTime DESC")
    List<StudentStruggle> findHighSeverityStruggles(@Param("minSeverity") Integer minSeverity);

    // RISK-14 (GDPR): brise borbe ciji je startTime stariji od zadatog datuma
    @org.springframework.data.jpa.repository.Modifying
    @Query("DELETE FROM StudentStruggle s WHERE s.startTime < :cutoff")
    int deleteByStartTimeBefore(@Param("cutoff") java.time.LocalDateTime cutoff);
}