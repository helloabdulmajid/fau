package in.abdulmajid.fau.offer.entity;

import in.abdulmajid.fau.benefit.entity.BenefitRule;

import in.abdulmajid.fau.card.entity.Card;
import in.abdulmajid.fau.card.enums.CardNetwork;

import in.abdulmajid.fau.category.entity.Category;

import in.abdulmajid.fau.common.BaseEntity;
import in.abdulmajid.fau.common.enums.BenefitPeriod;

import in.abdulmajid.fau.merchant.entity.Merchant;

import in.abdulmajid.fau.offer.enums.OfferPlatform;
import in.abdulmajid.fau.offer.enums.OfferType;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Offer extends BaseEntity {

    // =========================================================
    // BASIC OFFER DETAILS
    // =========================================================

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private OfferType offerType;

    // Example:
    // 5 = 5%
    // 10 = 10%

    private Double value;

    private Boolean active = true;

    // =========================================================
    // BENEFIT DETAILS
    // =========================================================

    // Example:
    // Max cashback ₹500

    private Double maxBenefit;

    // Example:
    // Minimum spend ₹2000

    private Double minimumSpend;

    // Example:
    // Monthly cashback cap ₹1000

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
    // PLATFORM DETAILS
    // =========================================================

    @Enumerated(EnumType.STRING)
    private OfferPlatform platform;

    @Enumerated(EnumType.STRING)
    private BenefitPeriod benefitPeriod;

    @Enumerated(EnumType.STRING)
    private CardNetwork applicableNetwork;

    // =========================================================
    // SPECIAL CONDITIONS
    // =========================================================

    private Boolean weekendOnly = false;

    private Boolean onlineOnly = false;

    private Boolean requiresMembership = false;

    @Column(length = 1000)
    private String excludedMerchants;

    // =========================================================
    // EXTRA OFFER DETAILS
    // =========================================================

    @Column(length = 1000)
    private String milestoneBenefit;

    private Boolean limitedTimeOffer = false;

    private Integer priority = 0;

    private Boolean permanentOffer = false;

    // =========================================================
    // RELATIONS
    // =========================================================

    @ManyToOne
    private Card card;

    @ManyToOne
    private Merchant merchant;

    @ManyToOne
    private Category category;

    @ManyToOne
    private BenefitRule benefitRule;
}