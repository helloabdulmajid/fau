package in.abdulmajid.fau.offer.dto;

import in.abdulmajid.fau.card.enums.CardNetwork;

import in.abdulmajid.fau.common.enums.BenefitPeriod;

import in.abdulmajid.fau.offer.enums.OfferPlatform;
import in.abdulmajid.fau.offer.enums.OfferType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateOfferRequest {

    // =========================================================
    // BASIC OFFER DETAILS
    // =========================================================

    @NotBlank(message = "Offer title is required")
    private String title;

    private String description;

    @NotNull(message = "Offer type is required")
    private OfferType offerType;

    @NotNull(message = "Offer value is required")
    @Min(value = 0, message = "Offer value cannot be negative")
    private Double value;

    private Boolean active = true;

    // =========================================================
    // BENEFIT DETAILS
    // =========================================================

    @Min(value = 0, message = "Maximum benefit cannot be negative")
    private Double maxBenefit;

    @Min(value = 0, message = "Minimum spend cannot be negative")
    private Double minimumSpend;

    @Min(value = 0, message = "Cashback cap cannot be negative")
    private Double cashbackCap;

    // =========================================================
    // DATE DETAILS
    // =========================================================

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private LocalDate verifiedAt;

    // =========================================================
    // SOURCE DETAILS
    // =========================================================

    private String sourceUrl;

    // =========================================================
    // PLATFORM DETAILS
    // =========================================================

    @NotNull(message = "Offer platform is required")
    private OfferPlatform platform;

    @NotNull(message = "Benefit period is required")
    private BenefitPeriod benefitPeriod;

    private CardNetwork applicableNetwork;

    // =========================================================
    // SPECIAL CONDITIONS
    // =========================================================

    private Boolean weekendOnly = false;

    private Boolean onlineOnly = false;

    private Boolean requiresMembership = false;

    private String excludedMerchants;

    // =========================================================
    // EXTRA OFFER DETAILS
    // =========================================================

    private String milestoneBenefit;

    private Boolean limitedTimeOffer = false;

    private Integer priority = 0;

    private Boolean permanentOffer = false;

    // =========================================================
    // RELATIONS
    // =========================================================

    @NotNull(message = "Card ID is required")
    private Long cardId;

    @NotNull(message = "Merchant ID is required")
    private Long merchantId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Benefit rule ID is required")
    private Long benefitRuleId;
}