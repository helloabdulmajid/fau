package in.abdulmajid.cardiq.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SourceStats {
    private String source;
    private long count;
}
