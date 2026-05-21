package in.abdulmajid.cardiq.bank.controller;

import in.abdulmajid.cardiq.bank.dto.BankResponse;
import in.abdulmajid.cardiq.bank.dto.CreateBankRequest;
import in.abdulmajid.cardiq.bank.service.BankService;
import in.abdulmajid.cardiq.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banks")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @PostMapping
    public ApiResponse<BankResponse> createBank(
            @Valid @RequestBody CreateBankRequest request
    ) {

        return ApiResponse.<BankResponse>builder()
                .success(true)
                .message("Bank created successfully")
                .data(bankService.createBank(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<BankResponse>> getAllBanks() {

        return ApiResponse.<List<BankResponse>>builder()
                .success(true)
                .message("Banks fetched successfully")
                .data(bankService.getAllBanks())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BankResponse> getBankById(
            @PathVariable Long id
    ) {

        return ApiResponse.<BankResponse>builder()
                .success(true)
                .message("Bank fetched successfully")
                .data(bankService.getBankById(id))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<BankResponse> updateBank(
            @PathVariable Long id,
            @Valid @RequestBody CreateBankRequest request
    ) {

        return ApiResponse.<BankResponse>builder()
                .success(true)
                .message("Bank updated successfully")
                .data(bankService.updateBank(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBank(
            @PathVariable Long id
    ) {

        bankService.deleteBank(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Bank deleted successfully")
                .data(null)
                .build();
    }
}