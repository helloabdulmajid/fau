package in.abdulmajid.fau.benefit.dto;

import in.abdulmajid.fau.benefit.enums.BenefitType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBenefitRuleRequest {

    // =========================================================
    // BASIC DETAILS
    // =========================================================

    @NotBlank(message = "Rule name is required")
    private String name;

    // =========================================================
    // BENEFIT TYPE
    // =========================================================

    @NotNull(message = "Benefit type is required")
    private BenefitType benefitType;

    // =========================================================
    // REWARD POINT CONVERSION
    // =========================================================

    // Example:
    // 1 RP = ₹0.25

    private Double rewardPointConversion;

    // =========================================================
    // REDEMPTION RULES
    // =========================================================

    private Double redemptionFee;

    private Integer minimumRedemptionPoints;
    // =========================================================
    // REDEMPTION RULES
    // =========================================================
    private Integer redemptionMultiplierPoints;
    // Example: Must be in multiples of 4000 points

    private Double maxRedemptionValuePerMonth;

    // =========================================================
    // EXPIRY RULES
    // =========================================================

    private Boolean expiryApplicable = false;

    private Integer expiryMonths;

    // =========================================================
    // EXTRA NOTES
    // =========================================================

    private String notes;
}