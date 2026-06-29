package in.abdulmajid.fau.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BrowserStats {
    private String name;
    private long count;
}
