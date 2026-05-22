package in.abdulmajid.cardiq.card.controller;

import in.abdulmajid.cardiq.card.dto.CardFilterRequest;
import in.abdulmajid.cardiq.card.dto.CardResponse;
import in.abdulmajid.cardiq.card.dto.CreateCardRequest;
import in.abdulmajid.cardiq.card.service.CardService;
import in.abdulmajid.cardiq.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ApiResponse<CardResponse> createCard(
            @Valid @RequestBody CreateCardRequest request
    ) {

        return ApiResponse.<CardResponse>builder()
                .success(true)
                .message("Card created successfully")
                .data(cardService.createCard(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<CardResponse>> getAllCards() {

        return ApiResponse.<List<CardResponse>>builder()
                .success(true)
                .message("Cards fetched successfully")
                .data(cardService.getAllCards())
                .build();
    }

    @PostMapping("/filter")
    public ApiResponse<List<CardResponse>> filterCards(
            @RequestBody CardFilterRequest filter
    ) {

        return ApiResponse.<List<CardResponse>>builder()
                .success(true)
                .message("Filtered cards fetched successfully")
                .data(cardService.filterCards(filter))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CardResponse> getCardById(
            @PathVariable Long id
    ) {

        return ApiResponse.<CardResponse>builder()
                .success(true)
                .message("Card fetched successfully")
                .data(cardService.getCardById(id))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CardResponse> updateCard(
            @PathVariable Long id,
            @Valid @RequestBody CreateCardRequest request
    ) {

        return ApiResponse.<CardResponse>builder()
                .success(true)
                .message("Card updated successfully")
                .data(cardService.updateCard(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCard(
            @PathVariable Long id
    ) {

        cardService.deleteCard(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Card deleted successfully")
                .data(null)
                .build();
    }
}