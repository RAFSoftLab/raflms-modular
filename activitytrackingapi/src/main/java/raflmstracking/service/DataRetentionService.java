package raflmstracking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raflmstracking.repository.StudentEventRepository;
import raflmstracking.repository.StudentSessionRepository;
import raflmstracking.repository.StudentStruggleRepository;

import java.time.LocalDateTime;

/**
 * RISK-14 fix (GDPR — retencija podataka):
 *
 * Brise student event-e, sesije i borbe koje su starije od
 * raflms.retention.days dana (default: 730 = 2 godine).
 *
 * Pokrecе se svakog dana u 03:00 (cron = "0 0 3 * * *").
 * Period se moze promeniti bez ponovnog deploymenta kroz env varijablu:
 *   export RAF_RETENTION_DAYS=365
 *
 * GDPR osnov: podaci o ponasanju studenata tokom ispita su osobni podaci.
 * Cuvanje vise od 2 godine nije opravdano svrhom (analiza akademske uspesnosti)
 * osim ako postoji izricita pravna osnova za duze cuvanje.
 */
@Service
public class DataRetentionService {

    private static final Logger log = LoggerFactory.getLogger(DataRetentionService.class);

    private final StudentEventRepository eventRepo;
    private final StudentSessionRepository sessionRepo;
    private final StudentStruggleRepository struggleRepo;

    @Value("${raflms.retention.days:730}")
    private int retentionDays;

    public DataRetentionService(StudentEventRepository eventRepo,
                                StudentSessionRepository sessionRepo,
                                StudentStruggleRepository struggleRepo) {
        this.eventRepo = eventRepo;
        this.sessionRepo = sessionRepo;
        this.struggleRepo = struggleRepo;
    }

    @Scheduled(cron = "0 0 3 * * *")  // svaki dan u 03:00
    @Transactional
    public void deleteExpiredData() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(retentionDays);
        log.info("GDPR retention job: brisanje podataka starijih od {} dana (pre {})", retentionDays, cutoff);

        int deletedEvents = eventRepo.deleteByTimestampBefore(cutoff);
        int deletedSessions = sessionRepo.deleteByStartTimeBefore(cutoff);
        int deletedStruggles = struggleRepo.deleteByStartTimeBefore(cutoff);

        log.info("GDPR retention job zavrsен: obrisano {} event-a, {} sesija, {} borbi",
                deletedEvents, deletedSessions, deletedStruggles);
    }
}
