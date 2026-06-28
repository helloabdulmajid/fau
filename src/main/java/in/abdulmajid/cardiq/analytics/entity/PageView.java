package in.abdulmajid.cardiq.analytics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "page_view", indexes = {
    @Index(name = "idx_page_view_created", columnList = "createdAt"),
    @Index(name = "idx_page_view_path", columnList = "path"),
    @Index(name = "idx_page_view_session", columnList = "sessionId")
})
public class PageView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String url;

    @Column(length = 255)
    private String path;

    @Column(columnDefinition = "TEXT")
    private String queryString;

    @Column(length = 500)
    private String referrer;

    @Column(length = 500)
    private String userAgent;

    @Column(length = 64)
    private String ipHash;

    @Column(length = 36)
    private String sessionId;

    private Long userId;

    @Column(length = 20)
    private String deviceType;

    @Column(length = 50)
    private String browser;

    @Column(length = 50)
    private String os;

    @Column(length = 5)
    private String countryCode;

    private Long durationMs;

    private int statusCode;

    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
