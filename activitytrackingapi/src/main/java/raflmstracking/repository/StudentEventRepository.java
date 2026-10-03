package raflmstracking.repository;

import raflmstracking.model.StudentEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StudentEventRepository extends JpaRepository<StudentEvent, Long> {

    List<StudentEvent> findByStudentIdOrderByTimestampAsc(String studentId);

    List<StudentEvent> findBySessionIdOrderByTimestampAsc(String sessionId);

    List<StudentEvent> findByEventTypeOrderByTimestampDesc(String eventType);

    @Query("SELECT e FROM StudentEvent e WHERE e.studentId = :studentId AND e.timestamp BETWEEN :startTime AND :endTime ORDER BY e.timestamp ASC")
    List<StudentEvent> findByStudentIdAndTimestampBetween(@Param("studentId") String studentId,
                                                          @Param("startTime") LocalDateTime startTime,
                                                          @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COUNT(e) FROM StudentEvent e WHERE e.studentId = :studentId AND e.eventType = :eventType")
    Long countByStudentIdAndEventType(@Param("studentId") String studentId, @Param("eventType") String eventType);

    // RISK-14 (GDPR): brise event-e starije od zadatog datuma
    @org.springframework.data.jpa.repository.Modifying
    @Query("DELETE FROM StudentEvent e WHERE e.timestamp < :cutoff")
    int deleteByTimestampBefore(@Param("cutoff") java.time.LocalDateTime cutoff);
}
