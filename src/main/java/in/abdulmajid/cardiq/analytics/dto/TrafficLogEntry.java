package in.abdulmajid.cardiq.analytics.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class TrafficLogEntry {
    private Long id;
    private String path;
    private String referrer;
    private String deviceType;
    private String browser;
    private String os;
    private String countryCode;
    private int statusCode;
    private Instant createdAt;
}
