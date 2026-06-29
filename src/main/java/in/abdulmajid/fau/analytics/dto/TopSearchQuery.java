package in.abdulmajid.fau.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopSearchQuery {
    private String keyword;
    private long count;
}
