package in.abdulmajid.cardiq.analytics.repository;

import in.abdulmajid.cardiq.analytics.entity.PageView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface PageViewRepository extends JpaRepository<PageView, Long> {

    long countByCreatedAtBetween(Instant from, Instant to);

    @Query("SELECT COUNT(DISTINCT p.ipHash) FROM PageView p WHERE p.createdAt BETWEEN :from AND :to")
    long countUniqueVisitors(@Param("from") Instant from, @Param("to") Instant to);

    @Query("SELECT p.path, COUNT(p) as cnt FROM PageView p WHERE p.createdAt BETWEEN :from AND :to GROUP BY p.path ORDER BY cnt DESC")
    List<Object[]> findTopPages(@Param("from") Instant from, @Param("to") Instant to);

    @Query("SELECT p.deviceType, COUNT(p) as cnt FROM PageView p WHERE p.createdAt BETWEEN :from AND :to AND p.deviceType IS NOT NULL GROUP BY p.deviceType ORDER BY cnt DESC")
    List<Object[]> findDeviceBreakdown(@Param("from") Instant from, @Param("to") Instant to);

    @Query("SELECT p.browser, COUNT(p) as cnt FROM PageView p WHERE p.createdAt BETWEEN :from AND :to AND p.browser IS NOT NULL GROUP BY p.browser ORDER BY cnt DESC")
    List<Object[]> findBrowserBreakdown(@Param("from") Instant from, @Param("to") Instant to);

    @Query("SELECT p.os, COUNT(p) as cnt FROM PageView p WHERE p.createdAt BETWEEN :from AND :to AND p.os IS NOT NULL GROUP BY p.os ORDER BY cnt DESC")
    List<Object[]> findOsBreakdown(@Param("from") Instant from, @Param("to") Instant to);

    @Query("SELECT COALESCE(p.referrer, 'direct') as src, COUNT(p) as cnt FROM PageView p WHERE p.createdAt BETWEEN :from AND :to GROUP BY src ORDER BY cnt DESC")
    List<Object[]> findSourceBreakdown(@Param("from") Instant from, @Param("to") Instant to);

    @Query("SELECT FUNCTION('date_trunc', 'day', p.createdAt) as day, COUNT(p) FROM PageView p WHERE p.createdAt BETWEEN :from AND :to GROUP BY day ORDER BY day")
    List<Object[]> findDailyTraffic(@Param("from") Instant from, @Param("to") Instant to);

    @Query("SELECT p FROM PageView p ORDER BY p.createdAt DESC")
    List<PageView> findRecentPageViews();

    @Query("SELECT p.ipHash, COUNT(p) as cnt FROM PageView p WHERE p.createdAt BETWEEN :from AND :to AND p.ipHash IS NOT NULL GROUP BY p.ipHash ORDER BY cnt DESC")
    List<Object[]> findTopIps(@Param("from") Instant from, @Param("to") Instant to);

    void deleteByCreatedAtBefore(Instant before);
}
