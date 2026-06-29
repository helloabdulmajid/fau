package in.abdulmajid.fau.offer.dto;

import in.abdulmajid.fau.benefit.enums.BenefitType;

import in.abdulmajid.fau.card.enums.CardNetwork;

import in.abdulmajid.fau.common.enums.BenefitPeriod;

import in.abdulmajid.fau.offer.enums.OfferPlatform;
import in.abdulmajid.fau.offer.enums.OfferType;
import in.abdulmajid.fau.benefit.enums.BenefitType;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class OfferResponse {

    // =========================================================
    // BASIC OFFER DETAILS
    // =========================================================

    private Long id;

    private String title;

    private String description;

    private OfferType offerType;

    private Double value;

    private Boolean active;

    // =========================================================
    // BENEFIT DETAILS
    // =========================================================

    private Double maxBenefit;

    private Double minimumSpend;

    private Double cashbackCap;

    // =========================================================
    // DATE DETAILS
    // =========================================================

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate verifiedAt;

    // =========================================================
    // SOURCE DETAILS
    // =========================================================

    private String sourceUrl;

    // =========================================================
    // RELATION DETAILS
    // =========================================================

    private String cardName;

    private String merchantName;

    private String categoryName;

    // =========================================================
    // PLATFORM DETAILS
    // =========================================================

    private OfferPlatform platform;

    private BenefitPeriod benefitPeriod;

    private CardNetwork applicableNetwork;

    // =========================================================
    // SPECIAL CONDITIONS
    // =========================================================

    private Boolean weekendOnly;

    private Boolean onlineOnly;

    private Boolean requiresMembership;

    private String excludedMerchants;

    // =========================================================
    // EXTRA OFFER DETAILS
    // =========================================================

    private String milestoneBenefit;

    private Boolean limitedTimeOffer;

    private Integer priority;

    private Boolean permanentOffer;

    // =========================================================
    // BENEFIT RULE DETAILS
    // =========================================================

    private String benefitRuleName;

    private BenefitType benefitType;
}