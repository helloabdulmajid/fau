package in.abdulmajid.cardiq.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopSearchQuery {
    private String keyword;
    private long count;
}
