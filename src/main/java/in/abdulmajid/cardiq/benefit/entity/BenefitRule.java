package in.abdulmajid.cardiq.benefit.entity;

import in.abdulmajid.cardiq.benefit.enums.BenefitType;
import in.abdulmajid.cardiq.common.BaseEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BenefitRule extends BaseEntity {

    // =========================================================
    // BASIC DETAILS
    // =========================================================

    // Example:
    // Cashback Rule
    // Reward Points Rule

    private String name;

    // =========================================================
    // BENEFIT CREDIT TYPE
    // =========================================================

    @Enumerated(EnumType.STRING)
    private BenefitType benefitType;

    // =========================================================
    // REWARD POINT CONVERSION
    // =========================================================

    // Example:
    // 1 RP = ₹0.25

    private Double rewardPointConversion;

    // =========================================================
    // MINIMUM REDEMPTION RULE
    // =========================================================

    private Integer minimumRedemptionPoints;
    // Example: Minimum 1000 points

    // =========================================================
    // EXPIRY RULES
    // =========================================================

    // Example:
    // true = points expire
    // false = lifetime points

    private Boolean expiryApplicable = false;

    // Example:
    // reward points expire after 24 months

    private Integer expiryMonths;
    // =========================================================
    // REDEMPTION RULES
    // =========================================================
    private Integer redemptionMultiplierPoints;
    // Example: Must be in multiples of 4000 points

    private Double maxRedemptionValuePerMonth;
    // Example: Max ₹3000 cashback per month
    // =========================================================
    // REDEMPTION FEES
    // =========================================================

    private Double redemptionFee;
    // Example: ₹99 redemption fee

    // =========================================================
    // EXTRA NOTES
    // =========================================================

    @Column(length = 1000)
    private String notes;
}