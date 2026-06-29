package in.abdulmajid.fau.analytics.service;

import in.abdulmajid.fau.analytics.dto.*;
import in.abdulmajid.fau.analytics.entity.DailyStat;
import in.abdulmajid.fau.analytics.entity.PageView;
import in.abdulmajid.fau.analytics.entity.SearchLog;
import in.abdulmajid.fau.analytics.repository.DailyStatRepository;
import in.abdulmajid.fau.analytics.repository.PageViewRepository;
import in.abdulmajid.fau.analytics.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final PageViewRepository pageViewRepository;
    private final SearchLogRepository searchLogRepository;
    private final DailyStatRepository dailyStatRepository;

    @Async
    public void recordPageView(String url, String path, String queryString, String referrer,
                               String userAgent, String ipHash, String sessionId, Long userId,
                               String deviceType, String browser, String os, String countryCode,
                               int statusCode) {
        try {
            PageView pv = new PageView();
            pv.setUrl(url);
            pv.setPath(path);
            pv.setQueryString(queryString);
            pv.setReferrer(referrer);
            pv.setUserAgent(userAgent);
            pv.setIpHash(ipHash);
            pv.setSessionId(sessionId);
            pv.setUserId(userId);
            pv.setDeviceType(deviceType);
            pv.setBrowser(browser);
            pv.setOs(os);
            pv.setCountryCode(countryCode);
            pv.setStatusCode(statusCode);
            pageViewRepository.save(pv);
        } catch (Exception e) {
            log.warn("Failed to record page view: {}", e.getMessage());
        }
    }

    @Async
    public void logSearch(String keyword, int resultCount, String referrer,
                          String sessionId, Long userId, String deviceType) {
        try {
            SearchLog sl = new SearchLog();
            sl.setKeyword(keyword);
            sl.setResultCount(resultCount);
            sl.setSource(referrer);
            sl.setSessionId(sessionId);
            sl.setUserId(userId);
            sl.setDeviceType(deviceType);
            searchLogRepository.save(sl);
        } catch (Exception e) {
            log.warn("Failed to log search: {}", e.getMessage());
        }
    }

    public AnalyticsOverview getOverview(LocalDate from, LocalDate to) {
        Instant fromInstant = from.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant toInstant = to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        LocalDate prevFrom = from.minusDays(to.toEpochDay() - from.toEpochDay() + 1);
        LocalDate prevTo = from.minusDays(1);
        Instant prevFromInstant = prevFrom.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant prevToInstant = prevTo.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        long views = pageViewRepository.countByCreatedAtBetween(fromInstant, toInstant);
        long uniqueVisitors = pageViewRepository.countUniqueVisitors(fromInstant, toInstant);
        long searches = searchLogRepository.countByCreatedAtBetween(fromInstant, toInstant);

        long prevViews = pageViewRepository.countByCreatedAtBetween(prevFromInstant, prevToInstant);
        long prevUniqueVisitors = pageViewRepository.countUniqueVisitors(prevFromInstant, prevToInstant);
        long prevSearches = searchLogRepository.countByCreatedAtBetween(prevFromInstant, prevToInstant);

        double bounceRate = views > 0 ? Math.round((double) (views - uniqueVisitors) / views * 100) / 100.0 : 0;
        double prevBounceRate = prevViews > 0 ? Math.round((double) (prevViews - prevUniqueVisitors) / prevViews * 100) / 100.0 : 0;

        return AnalyticsOverview.builder()
                .totalViews(views).uniqueVisitors(uniqueVisitors).totalSearches(searches)
                .bounceRate(bounceRate).avgSessionDurationSec(0)
                .prevTotalViews(prevViews).prevUniqueVisitors(prevUniqueVisitors)
                .prevTotalSearches(prevSearches).prevBounceRate(prevBounceRate)
                .prevAvgSessionDurationSec(0).build();
    }

    public List<TrafficPoint> getTraffic(LocalDate from, LocalDate to) {
        Instant fromInstant = from.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant toInstant = to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        List<Object[]> dailyViews = pageViewRepository.findDailyTraffic(fromInstant, toInstant);
        List<Object[]> dailySearches = searchLogRepository.findDailySearchCount(fromInstant, toInstant);

        Map<LocalDate, Long> viewsMap = new HashMap<>();
        Map<LocalDate, Long> searchesMap = new HashMap<>();

        for (Object[] row : dailyViews) {
            LocalDate day = ((java.sql.Timestamp) row[0]).toLocalDateTime().toLocalDate();
            viewsMap.put(day, (Long) row[1]);
        }
        for (Object[] row : dailySearches) {
            LocalDate day = ((java.sql.Timestamp) row[0]).toLocalDateTime().toLocalDate();
            searchesMap.put(day, (Long) row[1]);
        }

        List<TrafficPoint> points = new ArrayList<>();
        LocalDate current = from;
        while (!current.isAfter(to)) {
            points.add(new TrafficPoint(
                    current.toString(),
                    viewsMap.getOrDefault(current, 0L),
                    0,
                    searchesMap.getOrDefault(current, 0L)
            ));
            current = current.plusDays(1);
        }
        return points;
    }

    public List<TopSearchQuery> getTopKeywords(LocalDate from, LocalDate to) {
        Instant fromInstant = from.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant toInstant = to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        return searchLogRepository.findTopKeywords(fromInstant, toInstant).stream()
                .map(row -> new TopSearchQuery((String) row[0], (Long) row[1]))
                .limit(20).collect(Collectors.toList());
    }

    public List<ZeroResultQuery> getZeroResultKeywords(LocalDate from, LocalDate to) {
        Instant fromInstant = from.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant toInstant = to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        return searchLogRepository.findZeroResultKeywords(fromInstant, toInstant).stream()
                .map(row -> new ZeroResultQuery((String) row[0], (Long) row[1]))
                .limit(20).collect(Collectors.toList());
    }

    public List<TopPageView> getTopPages(LocalDate from, LocalDate to) {
        Instant fromInstant = from.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant toInstant = to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        return pageViewRepository.findTopPages(fromInstant, toInstant).stream()
                .map(row -> new TopPageView((String) row[0], (Long) row[1]))
                .limit(20).collect(Collectors.toList());
    }

    public List<DeviceStats> getDeviceBreakdown(LocalDate from, LocalDate to) {
        Instant fromInstant = from.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant toInstant = to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        return pageViewRepository.findDeviceBreakdown(fromInstant, toInstant).stream()
                .map(row -> new DeviceStats((String) row[0], (Long) row[1]))
                .collect(Collectors.toList());
    }

    public List<BrowserStats> getBrowserBreakdown(LocalDate from, LocalDate to) {
        Instant fromInstant = from.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant toInstant = to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        return pageViewRepository.findBrowserBreakdown(fromInstant, toInstant).stream()
                .map(row -> new BrowserStats((String) row[0], (Long) row[1]))
                .collect(Collectors.toList());
    }

    public List<OsStats> getOsBreakdown(LocalDate from, LocalDate to) {
        Instant fromInstant = from.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant toInstant = to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        return pageViewRepository.findOsBreakdown(fromInstant, toInstant).stream()
                .map(row -> new OsStats((String) row[0], (Long) row[1]))
                .collect(Collectors.toList());
    }

    public List<SourceStats> getSourceBreakdown(LocalDate from, LocalDate to) {
        Instant fromInstant = from.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant toInstant = to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        return pageViewRepository.findSourceBreakdown(fromInstant, toInstant).stream()
                .map(row -> new SourceStats((String) row[0], (Long) row[1]))
                .collect(Collectors.toList());
    }

    public List<AnomalyAlert> getAnomalies(LocalDate from, LocalDate to) {
        Instant fromInstant = from.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant toInstant = to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        List<AnomalyAlert> alerts = new ArrayList<>();

        List<Object[]> topIps = pageViewRepository.findTopIps(fromInstant, toInstant);
        for (Object[] row : topIps) {
            String ipHash = (String) row[0];
            Long count = (Long) row[1];
            if (count > 100) {
                alerts.add(AnomalyAlert.builder()
                        .type("RAPID_REQUESTS").severity("warning")
                        .message("Rapid requests from single visitor")
                        .detail(ipHash + " — " + count + " requests in selected period")
                        .detectedAt(Instant.now()).build());
            }
        }

        List<Object[]> topPages = pageViewRepository.findTopPages(fromInstant, toInstant);
        if (!topPages.isEmpty()) {
            Object[] top = topPages.get(0);
            String topPath = (String) top[0];
            Long topCount = (Long) top[1];
            if (topCount > 50) {
                alerts.add(AnomalyAlert.builder()
                        .type("TRAFFIC_SPIKE").severity("info")
                        .message("High traffic on " + topPath)
                        .detail(topCount + " views — check for viral content or scraping")
                        .detectedAt(Instant.now()).build());
            }
        }

        return alerts;
    }

    public List<TrafficLogEntry> getRecentLogs(int limit) {
        return pageViewRepository.findRecentPageViews().stream()
                .limit(limit)
                .map(pv -> TrafficLogEntry.builder()
                        .id(pv.getId()).path(pv.getPath())
                        .referrer(pv.getReferrer()).deviceType(pv.getDeviceType())
                        .browser(pv.getBrowser()).os(pv.getOs())
                        .countryCode(pv.getCountryCode()).statusCode(pv.getStatusCode())
                        .createdAt(pv.getCreatedAt()).build())
                .collect(Collectors.toList());
    }

    public void pruneOldData(int retentionDays) {
        Instant cutoff = Instant.now().minus(java.time.Duration.ofDays(retentionDays));
        pageViewRepository.deleteByCreatedAtBefore(cutoff);
        searchLogRepository.deleteByCreatedAtBefore(cutoff);
        log.info("Pruned analytics data older than {} days", retentionDays);
    }

    public void buildDailyStat(LocalDate date) {
        Instant from = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant to = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        DailyStat stat = dailyStatRepository.findByStatDate(date).orElse(new DailyStat());
        stat.setStatDate(date);
        stat.setTotalViews(pageViewRepository.countByCreatedAtBetween(from, to));
        stat.setUniqueVisitors(pageViewRepository.countUniqueVisitors(from, to));
        stat.setTotalSearches(searchLogRepository.countByCreatedAtBetween(from, to));
        stat.setZeroResultSearches(searchLogRepository.findZeroResultKeywords(from, to).size());

        List<Object[]> topKeywords = searchLogRepository.findTopKeywords(from, to);
        if (!topKeywords.isEmpty()) {
            stat.setTopKeywordsJson(toJson(topKeywords.subList(0, Math.min(10, topKeywords.size()))));
        }

        List<Object[]> topPages = pageViewRepository.findTopPages(from, to);
        if (!topPages.isEmpty()) {
            stat.setTopPagesJson(toJson(topPages.subList(0, Math.min(10, topPages.size()))));
        }

        stat.setDeviceBreakdownJson(toJson(pageViewRepository.findDeviceBreakdown(from, to)));
        stat.setBrowserBreakdownJson(toJson(pageViewRepository.findBrowserBreakdown(from, to)));
        stat.setOsBreakdownJson(toJson(pageViewRepository.findOsBreakdown(from, to)));
        stat.setSourceBreakdownJson(toJson(pageViewRepository.findSourceBreakdown(from, to)));

        dailyStatRepository.save(stat);
    }

    private String toJson(List<Object[]> rows) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < rows.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("{\"k\":\"").append(rows.get(i)[0]).append("\",\"v\":").append(rows.get(i)[1]).append("}");
        }
        sb.append("]");
        return sb.toString();
    }
}
