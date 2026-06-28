package in.abdulmajid.cardiq.search.controller;

import in.abdulmajid.cardiq.analytics.service.AnalyticsService;
import in.abdulmajid.cardiq.auth.entity.User;
import in.abdulmajid.cardiq.common.ApiResponse;
import in.abdulmajid.cardiq.search.dto.SearchCardResponse;
import in.abdulmajid.cardiq.search.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final AnalyticsService analyticsService;

    @GetMapping
    public ApiResponse<List<SearchCardResponse>> search(
            @RequestParam String keyword,
            @RequestParam(required = false) Double amount,
            @RequestParam(defaultValue = "all") String mode,
            @AuthenticationPrincipal User user,
            HttpServletRequest request
    ) {
        Long userId = (user != null) ? user.getId() : null;

        List<SearchCardResponse> results = searchService.search(keyword, amount, mode, userId);

        analyticsService.logSearch(
                keyword.toLowerCase().trim(),
                results.size(),
                request.getHeader("Referer"),
                (String) request.getAttribute("analytics_session_id"),
                userId,
                null
        );

        return ApiResponse.<List<SearchCardResponse>>builder()
                .success(true)
                .message("Search results fetched successfully")
                .data(results)
                .build();
    }
}
