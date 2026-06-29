package in.abdulmajid.fau.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TrafficPoint {
    private String date;
    private long views;
    private long uniqueVisitors;
    private long searches;
}
