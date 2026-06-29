package in.abdulmajid.fau.analytics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "daily_stat", uniqueConstraints = {
    @UniqueConstraint(columnNames = "statDate")
})
public class DailyStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate statDate;

    private long totalViews;

    private long uniqueVisitors;

    private long totalSearches;

    private long zeroResultSearches;

    @Column(columnDefinition = "TEXT")
    private String topPagesJson;

    @Column(columnDefinition = "TEXT")
    private String topKeywordsJson;

    @Column(columnDefinition = "TEXT")
    private String deviceBreakdownJson;

    @Column(columnDefinition = "TEXT")
    private String browserBreakdownJson;

    @Column(columnDefinition = "TEXT")
    private String osBreakdownJson;

    @Column(columnDefinition = "TEXT")
    private String sourceBreakdownJson;

    private long anomaliesDetected;
}
