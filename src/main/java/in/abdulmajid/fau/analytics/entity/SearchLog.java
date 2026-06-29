package in.abdulmajid.fau.analytics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "search_log", indexes = {
    @Index(name = "idx_search_log_created", columnList = "createdAt"),
    @Index(name = "idx_search_log_keyword", columnList = "keyword"),
    @Index(name = "idx_search_log_no_results", columnList = "resultCount")
})
public class SearchLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String keyword;

    private int resultCount;

    @Column(length = 500)
    private String source;

    @Column(length = 36)
    private String sessionId;

    private Long userId;

    @Column(length = 20)
    private String deviceType;

    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
