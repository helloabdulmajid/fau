package in.abdulmajid.fau.usercard.controller;

import in.abdulmajid.fau.auth.entity.User;
import in.abdulmajid.fau.common.ApiResponse;
import in.abdulmajid.fau.usercard.dto.AddCardRequest;
import in.abdulmajid.fau.usercard.dto.AddManualCardRequest;
import in.abdulmajid.fau.usercard.dto.UserCardResponse;
import in.abdulmajid.fau.usercard.service.UserCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/me/cards")
@RequiredArgsConstructor
public class UserCardController {

    private final UserCardService userCardService;

    @GetMapping
    public ApiResponse<List<UserCardResponse>> getMyCards(
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.<List<UserCardResponse>>builder()
                .success(true)
                .message("Cards fetched successfully")
                .data(userCardService.getMyCards(user.getId()))
                .build();
    }

    @PostMapping
    public ApiResponse<UserCardResponse> addCard(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody AddCardRequest request
    ) {
        return ApiResponse.<UserCardResponse>builder()
                .success(true)
                .message("Card added successfully")
                .data(userCardService.addCard(user, request))
                .build();
    }

    @PostMapping("/manual")
    public ApiResponse<UserCardResponse> addCardManual(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody AddManualCardRequest request
    ) {
        return ApiResponse.<UserCardResponse>builder()
                .success(true)
                .message("Card added successfully")
                .data(userCardService.addCardManual(user, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> removeCard(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        userCardService.removeCard(user.getId(), id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Card removed successfully")
                .build();
    }

    @PutMapping("/{id}/favorite")
    public ApiResponse<UserCardResponse> toggleFavorite(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        return ApiResponse.<UserCardResponse>builder()
                .success(true)
                .message("Favorite updated successfully")
                .data(userCardService.toggleFavorite(user.getId(), id))
                .build();
    }
}
