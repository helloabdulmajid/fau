package in.abdulmajid.fau.analytics.repository;

import in.abdulmajid.fau.analytics.entity.SearchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {

    long countByCreatedAtBetween(Instant from, Instant to);

    @Query("SELECT s.keyword, COUNT(s) as cnt FROM SearchLog s WHERE s.createdAt BETWEEN :from AND :to GROUP BY s.keyword ORDER BY cnt DESC")
    List<Object[]> findTopKeywords(@Param("from") Instant from, @Param("to") Instant to);

    @Query("SELECT s.keyword, COUNT(s) as cnt FROM SearchLog s WHERE s.createdAt BETWEEN :from AND :to AND s.resultCount = 0 GROUP BY s.keyword ORDER BY cnt DESC")
    List<Object[]> findZeroResultKeywords(@Param("from") Instant from, @Param("to") Instant to);

    @Query("SELECT FUNCTION('date_trunc', 'day', s.createdAt) as day, COUNT(s) FROM SearchLog s WHERE s.createdAt BETWEEN :from AND :to GROUP BY day ORDER BY day")
    List<Object[]> findDailySearchCount(@Param("from") Instant from, @Param("to") Instant to);

    void deleteByCreatedAtBefore(Instant before);
}
