package in.abdulmajid.cardiq.card.dto;

import in.abdulmajid.cardiq.card.enums.*;
import in.abdulmajid.cardiq.common.enums.BenefitPeriod;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCardRequest {

    // =========================================================
    // BASIC CARD DETAILS
    // =========================================================

    @NotBlank(message = "Card name is required")
    private String name;

    // Example:
    // sbi-cashback-credit-card

    private String slug;

    private String description;

    private String imageUrl;

    private Boolean active = true;

    private Boolean ltf = false;

    // =========================================================
    // CARD FEES
    // =========================================================

    @NotNull(message = "Joining fee is required")
    @Min(value = 0, message = "Joining fee cannot be negative")
    private Double joiningFee;

    @NotNull(message = "Annual fee is required")
    @Min(value = 0, message = "Annual fee cannot be negative")
    private Double annualFee;

    // Example:
    // 3.5 means 3.5% forex markup

    private Double forexMarkup;

    // =========================================================
    // CARD CLASSIFICATION
    // =========================================================

    @NotNull(message = "Card network is required")
    private CardNetwork network;

    @NotNull(message = "Card type is required")
    private CardType cardType;

    @NotNull(message = "Reward type is required")
    private RewardType rewardType;

    @NotNull(message = "Card level is required")
    private CardLevel cardLevel;

    @NotNull(message = "Reward value type is required")
    private RewardValueType rewardValueType;

    // =========================================================
    // REWARD DETAILS
    // =========================================================

    // Example:
    // 5 = 5% cashback
    // 10 = 10X reward points

    private Double baseRewardValue;

    // Example:
    // 1 RP = ₹0.25

    private Double rewardPointConversion;

    // Example:
    // Reward points expire after 24 months

    private Integer rewardPointExpiryMonths;

    // =========================================================
    // CARD FEATURES
    // =========================================================

    private Boolean emiAvailable = false;

    private Boolean fuelSurchargeWaiver = false;

    private Boolean airportLoungeAccess = false;

    private Boolean railwayLoungeAccess = false;

    private Boolean addOnCardAvailable = false;

    private Boolean contactlessEnabled = true;

    // =========================================================
    // LOUNGE ACCESS DETAILS
    // =========================================================

    // Example:
    // 8 lounges per year

    private Integer domesticLoungeAccess;

    // Example:
    // 4 international lounges per year

    private Integer internationalLoungeAccess;

    @Enumerated(EnumType.STRING)
    private BenefitPeriod domesticLoungePeriod;

    @Enumerated(EnumType.STRING)
    private BenefitPeriod internationalLoungePeriod;

    // =========================================================
    // CO-BRANDED CARD DETAILS
    // =========================================================

    private Boolean coBranded = false;

    // Example:
    // Swiggy
    // Amazon
    // Flipkart

    private String coBrandPartner;

    // =========================================================
    // ELIGIBILITY
    // =========================================================

    // Example:
    // 600000 annual income required

    private Integer minimumIncomeRequired;

    // =========================================================
    // Digital & Network Flags
    // =========================================================

    private Boolean upiEnabled = false;
    private Boolean isVirtualOnly = false;
    private Boolean requiresPrimaryCard = false;

    // =========================================================
    // RELATIONS
    // =========================================================

    @NotNull(message = "Bank ID is required")
    private Long bankId;
}