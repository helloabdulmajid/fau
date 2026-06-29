package in.abdulmajid.fau.benefit.controller;

import in.abdulmajid.fau.benefit.dto.BenefitRuleResponse;
import in.abdulmajid.fau.benefit.dto.CreateBenefitRuleRequest;
import in.abdulmajid.fau.benefit.service.BenefitRuleService;
import in.abdulmajid.fau.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/benefit-rules")
@RequiredArgsConstructor
public class BenefitRuleController {

    private final BenefitRuleService benefitRuleService;

    @PostMapping
    public ApiResponse<BenefitRuleResponse> createRule(
            @Valid @RequestBody
            CreateBenefitRuleRequest request
    ) {

        return ApiResponse
                .<BenefitRuleResponse>builder()
                .success(true)
                .message(
                        "Benefit rule created successfully"
                )
                .data(
                        benefitRuleService.createRule(request)
                )
                .build();
    }

    @GetMapping
    public ApiResponse<List<BenefitRuleResponse>>
    getAllRules() {

        return ApiResponse
                .<List<BenefitRuleResponse>>builder()
                .success(true)
                .message(
                        "Benefit rules fetched successfully"
                )
                .data(
                        benefitRuleService.getAllRules()
                )
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BenefitRuleResponse>
    getRuleById(
            @PathVariable Long id
    ) {

        return ApiResponse
                .<BenefitRuleResponse>builder()
                .success(true)
                .message(
                        "Benefit rule fetched successfully"
                )
                .data(
                        benefitRuleService.getRuleById(id)
                )
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<BenefitRuleResponse>
    updateRule(
            @PathVariable Long id,
            @Valid @RequestBody
            CreateBenefitRuleRequest request
    ) {

        return ApiResponse
                .<BenefitRuleResponse>builder()
                .success(true)
                .message(
                        "Benefit rule updated successfully"
                )
                .data(
                        benefitRuleService.updateRule(
                                id,
                                request
                        )
                )
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRule(
            @PathVariable Long id
    ) {

        benefitRuleService.deleteRule(id);

        return ApiResponse
                .<Void>builder()
                .success(true)
                .message(
                        "Benefit rule deleted successfully"
                )
                .data(null)
                .build();
    }
}