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
}