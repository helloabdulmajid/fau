package in.abdulmajid.fau.merchant.controller;

import in.abdulmajid.fau.common.ApiResponse;

import in.abdulmajid.fau.merchant.dto.CreateMerchantRequest;
import in.abdulmajid.fau.merchant.dto.MerchantResponse;
import in.abdulmajid.fau.merchant.service.MerchantService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/merchants")
@RequiredArgsConstructor
public class MerchantController {

    // =========================================================
    // SERVICES
    // =========================================================

    private final MerchantService merchantService;

    // =========================================================
    // CREATE NEW MERCHANT
    // =========================================================

    @PostMapping
    public ApiResponse<MerchantResponse> createMerchant(
            @Valid @RequestBody CreateMerchantRequest request
    ) {

        return ApiResponse.<MerchantResponse>builder()
                .success(true)
                .message("Merchant created successfully")
                .data(
                        merchantService.createMerchant(request)
                )
                .build();
    }

    // =========================================================
    // GET ALL MERCHANTS
    // =========================================================

    @GetMapping
    public ApiResponse<List<MerchantResponse>> getAllMerchants() {

        return ApiResponse.<List<MerchantResponse>>builder()
                .success(true)
                .message("Merchants fetched successfully")
                .data(
                        merchantService.getAllMerchants()
                )
                .build();
    }

    // =========================================================
    // GET SINGLE MERCHANT BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ApiResponse<MerchantResponse> getMerchantById(
            @PathVariable Long id
    ) {

        return ApiResponse.<MerchantResponse>builder()
                .success(true)
                .message("Merchant fetched successfully")
                .data(
                        merchantService.getMerchantById(id)
                )
                .build();
    }

    // =========================================================
    // UPDATE EXISTING MERCHANT
    // =========================================================

    @PutMapping("/{id}")
    public ApiResponse<MerchantResponse> updateMerchant(
            @PathVariable Long id,
            @Valid @RequestBody CreateMerchantRequest request
    ) {

        return ApiResponse.<MerchantResponse>builder()
                .success(true)
                .message("Merchant updated successfully")
                .data(
                        merchantService.updateMerchant(id, request)
                )
                .build();
    }

    // =========================================================
    // DELETE MERCHANT
    // =========================================================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMerchant(
            @PathVariable Long id
    ) {

        merchantService.deleteMerchant(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Merchant deleted successfully")
                .build();
    }
}