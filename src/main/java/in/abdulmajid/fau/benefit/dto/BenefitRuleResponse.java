package in.abdulmajid.fau.benefit.dto;

import in.abdulmajid.fau.benefit.enums.BenefitType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BenefitRuleResponse {

    // =========================================================
    // ID
    // =========================================================

    private Long id;

    // =========================================================
    // BASIC DETAILS
    // =========================================================

    private String name;

    // =========================================================
    // BENEFIT TYPE
    // =========================================================

    private BenefitType benefitType;

    // =========================================================
    // REWARD POINT CONVERSION
    // =========================================================

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

    private Boolean expiryApplicable;

    private Integer expiryMonths;

    // =========================================================
    // EXTRA NOTES
    // =========================================================

    private String notes;
}