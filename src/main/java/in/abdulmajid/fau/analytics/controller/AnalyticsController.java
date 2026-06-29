package in.abdulmajid.fau.analytics.controller;

import in.abdulmajid.fau.analytics.dto.*;
import in.abdulmajid.fau.analytics.service.AnalyticsService;
import in.abdulmajid.fau.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/overview")
    public ApiResponse<AnalyticsOverview> getOverview(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.<AnalyticsOverview>builder()
                .success(true).message("Overview fetched successfully")
                .data(analyticsService.getOverview(from, to)).build();
    }

    @GetMapping("/traffic")
    public ApiResponse<List<TrafficPoint>> getTraffic(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.<List<TrafficPoint>>builder()
                .success(true).message("Traffic data fetched successfully")
                .data(analyticsService.getTraffic(from, to)).build();
    }

    @GetMapping("/top-keywords")
    public ApiResponse<List<TopSearchQuery>> getTopKeywords(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.<List<TopSearchQuery>>builder()
                .success(true).message("Top keywords fetched successfully")
                .data(analyticsService.getTopKeywords(from, to)).build();
    }

    @GetMapping("/zero-result-queries")
    public ApiResponse<List<ZeroResultQuery>> getZeroResultQueries(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.<List<ZeroResultQuery>>builder()
                .success(true).message("Zero result queries fetched successfully")
                .data(analyticsService.getZeroResultKeywords(from, to)).build();
    }

    @GetMapping("/top-pages")
    public ApiResponse<List<TopPageView>> getTopPages(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.<List<TopPageView>>builder()
                .success(true).message("Top pages fetched successfully")
                .data(analyticsService.getTopPages(from, to)).build();
    }

    @GetMapping("/devices")
    public ApiResponse<List<DeviceStats>> getDeviceBreakdown(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.<List<DeviceStats>>builder()
                .success(true).message("Device breakdown fetched successfully")
                .data(analyticsService.getDeviceBreakdown(from, to)).build();
    }

    @GetMapping("/browsers")
    public ApiResponse<List<BrowserStats>> getBrowserBreakdown(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.<List<BrowserStats>>builder()
                .success(true).message("Browser breakdown fetched successfully")
                .data(analyticsService.getBrowserBreakdown(from, to)).build();
    }

    @GetMapping("/os")
    public ApiResponse<List<OsStats>> getOsBreakdown(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.<List<OsStats>>builder()
                .success(true).message("OS breakdown fetched successfully")
                .data(analyticsService.getOsBreakdown(from, to)).build();
    }

    @GetMapping("/sources")
    public ApiResponse<List<SourceStats>> getSourceBreakdown(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.<List<SourceStats>>builder()
                .success(true).message("Source breakdown fetched successfully")
                .data(analyticsService.getSourceBreakdown(from, to)).build();
    }

    @GetMapping("/anomalies")
    public ApiResponse<List<AnomalyAlert>> getAnomalies(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.<List<AnomalyAlert>>builder()
                .success(true).message("Anomalies fetched successfully")
                .data(analyticsService.getAnomalies(from, to)).build();
    }

    @GetMapping("/logs")
    public ApiResponse<List<TrafficLogEntry>> getRecentLogs(@RequestParam(defaultValue = "50") int limit) {
        return ApiResponse.<List<TrafficLogEntry>>builder()
                .success(true).message("Recent logs fetched successfully")
                .data(analyticsService.getRecentLogs(limit)).build();
    }
}
