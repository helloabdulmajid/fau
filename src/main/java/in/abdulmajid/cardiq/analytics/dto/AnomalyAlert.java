package in.abdulmajid.cardiq.analytics.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class AnomalyAlert {
    private String type;
    private String severity;
    private String message;
    private String detail;
    private Instant detectedAt;
}
