package in.abdulmajid.cardiq.merchant.controller;

import in.abdulmajid.cardiq.common.ApiResponse;
import in.abdulmajid.cardiq.merchant.dto.CreateMerchantRequest;
import in.abdulmajid.cardiq.merchant.dto.MerchantResponse;
import in.abdulmajid.cardiq.merchant.service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/merchants")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @PostMapping
    public ApiResponse<MerchantResponse> createMerchant(
            @Valid @RequestBody CreateMerchantRequest request
    ) {

        return ApiResponse.<MerchantResponse>builder()
                .success(true)
                .message("Merchant created successfully")
                .data(merchantService.createMerchant(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<MerchantResponse>> getAllMerchants() {

        return ApiResponse.<List<MerchantResponse>>builder()
                .success(true)
                .message("Merchants fetched successfully")
                .data(merchantService.getAllMerchants())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<MerchantResponse> getMerchantById(
            @PathVariable Long id
    ) {

        return ApiResponse.<MerchantResponse>builder()
                .success(true)
                .message("Merchant fetched successfully")
                .data(merchantService.getMerchantById(id))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<MerchantResponse> updateMerchant(
            @PathVariable Long id,
            @Valid @RequestBody CreateMerchantRequest request
    ) {

        return ApiResponse.<MerchantResponse>builder()
                .success(true)
                .message("Merchant updated successfully")
                .data(merchantService.updateMerchant(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMerchant(
            @PathVariable Long id
    ) {

        merchantService.deleteMerchant(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Merchant deleted successfully")
                .data(null)
                .build();
    }
}