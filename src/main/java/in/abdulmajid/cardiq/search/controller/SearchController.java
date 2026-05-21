package in.abdulmajid.cardiq.search.controller;

import in.abdulmajid.cardiq.common.ApiResponse;
import in.abdulmajid.cardiq.search.dto.SearchCardResponse;
import in.abdulmajid.cardiq.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ApiResponse<List<SearchCardResponse>> search(
            @RequestParam String keyword
    ) {

        return ApiResponse.<List<SearchCardResponse>>builder()
                .success(true)
                .message("Search results fetched successfully")
                .data(searchService.search(keyword))
                .build();
    }
}