package in.abdulmajid.cardiq.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopPageView {
    private String path;
    private long views;
}
