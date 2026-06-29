package in.abdulmajid.fau.analytics.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalyticsOverview {
    private long totalViews;
    private long uniqueVisitors;
    private long totalSearches;
    private double bounceRate;
    private double avgSessionDurationSec;

    private long prevTotalViews;
    private long prevUniqueVisitors;
    private long prevTotalSearches;
    private double prevBounceRate;
    private double prevAvgSessionDurationSec;
}
