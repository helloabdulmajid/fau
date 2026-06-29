package in.abdulmajid.fau.analytics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyticsScheduler {

    private final AnalyticsService analyticsService;

    @Scheduled(cron = "0 0 2 * * ?")
    public void aggregateDailyStats() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        log.info("Aggregating daily stats for {}", yesterday);
        analyticsService.buildDailyStat(yesterday);
    }

    @Scheduled(cron = "0 30 3 * * ?")
    public void pruneOldData() {
        log.info("Pruning analytics data older than 60 days");
        analyticsService.pruneOldData(60);
    }
}
