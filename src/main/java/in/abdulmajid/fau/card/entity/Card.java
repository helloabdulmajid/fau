package in.abdulmajid.fau.card.entity;

import in.abdulmajid.fau.bank.entity.Bank;
import in.abdulmajid.fau.card.enums.*;
import in.abdulmajid.fau.common.BaseEntity;
import in.abdulmajid.fau.common.enums.BenefitPeriod;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Card extends BaseEntity {

    // =========================================================
    // BASIC CARD DETAILS
    // =========================================================

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String slug;

    @Column(length = 1000)
    private String description;

    private String imageUrl;

    private Boolean active = true;

    private Boolean ltf = false;

    // =========================================================
    // CARD FEES
    // =========================================================

    private Double joiningFee;

    private Double annualFee;

    private Double forexMarkup;

    // =========================================================
    // CARD CLASSIFICATION
    // =========================================================

    @Enumerated(EnumType.STRING)
    private CardNetwork network;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    private CardLevel cardLevel;

    @Enumerated(EnumType.STRING)
    private RewardType rewardType;

    // =========================================================
    // Digital & Network Flags
    // =========================================================

    private Boolean upiEnabled = false;
    private Boolean isVirtualOnly = false;
    private Boolean requiresPrimaryCard = false;

    // =========================================================
    // REWARD DETAILS
    // =========================================================

    // Example:
    // 5 means 5% cashback
    // 10 means 10X reward points
    private Double baseRewardValue;
    @Enumerated(EnumType.STRING)
    private RewardValueType rewardValueType;

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

    private Integer domesticLoungeAccess;

    private Integer internationalLoungeAccess;

    @Enumerated(EnumType.STRING)
    private BenefitPeriod domesticLoungePeriod;

    @Enumerated(EnumType.STRING)
    private BenefitPeriod internationalLoungePeriod;

    // =========================================================
    // CO-BRANDED CARD DETAILS
    // =========================================================

    private Boolean coBranded = false;

    private String coBrandPartner;

    // =========================================================
    // ELIGIBILITY
    // =========================================================

    private Integer minimumIncomeRequired;

    // =========================================================
    // RELATIONS
    // =========================================================

    @ManyToOne
    private Bank bank;

    // private Integer monthlySpendWaiver;

    // private Double cashbackCap;

    // private Integer milestoneSpendTarget;

}