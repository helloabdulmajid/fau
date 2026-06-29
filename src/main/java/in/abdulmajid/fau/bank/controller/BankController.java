package in.abdulmajid.fau.bank.controller;

import in.abdulmajid.fau.bank.dto.BankResponse;
import in.abdulmajid.fau.bank.dto.CreateBankRequest;
import in.abdulmajid.fau.bank.service.BankService;
import in.abdulmajid.fau.common.ApiResponse;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banks")
@RequiredArgsConstructor
public class BankController {

    // =========================================================
    // SERVICES
    // =========================================================

    private final BankService bankService;

    // =========================================================
    // CREATE NEW BANK
    // =========================================================

    @PostMapping
    public ApiResponse<BankResponse> createBank(
            @Valid @RequestBody CreateBankRequest request
    ) {

        return ApiResponse.<BankResponse>builder()
                .success(true)
                .message("Bank created successfully")
                .data(
                        bankService.createBank(request)
                )
                .build();
    }

    // =========================================================
    // GET ALL BANKS
    // =========================================================

    @GetMapping
    public ApiResponse<List<BankResponse>> getAllBanks() {

        return ApiResponse.<List<BankResponse>>builder()
                .success(true)
                .message("Banks fetched successfully")
                .data(
                        bankService.getAllBanks()
                )
                .build();
    }

    // =========================================================
    // GET SINGLE BANK BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ApiResponse<BankResponse> getBankById(
            @PathVariable Long id
    ) {

        return ApiResponse.<BankResponse>builder()
                .success(true)
                .message("Bank fetched successfully")
                .data(
                        bankService.getBankById(id)
                )
                .build();
    }

    // =========================================================
    // UPDATE EXISTING BANK
    // =========================================================

    @PutMapping("/{id}")
    public ApiResponse<BankResponse> updateBank(
            @PathVariable Long id,
            @Valid @RequestBody CreateBankRequest request
    ) {

        return ApiResponse.<BankResponse>builder()
                .success(true)
                .message("Bank updated successfully")
                .data(
                        bankService.updateBank(id, request)
                )
                .build();
    }

    // =========================================================
    // DELETE BANK
    // =========================================================

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBank(
            @PathVariable Long id
    ) {

        bankService.deleteBank(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Bank deleted successfully")
                .build();
    }
}