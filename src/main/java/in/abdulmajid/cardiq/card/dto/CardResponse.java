package in.abdulmajid.cardiq.card.dto;

import in.abdulmajid.cardiq.card.enums.*;
import in.abdulmajid.cardiq.common.enums.BenefitPeriod;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CardResponse {

    // =========================================================
    // BASIC CARD DETAILS
    // =========================================================

    private Long id;

    private String name;

    private String slug;

    private String description;

    private String imageUrl;

    private Boolean active;

    private Boolean ltf;

    // =========================================================
    // BANK DETAILS
    // =========================================================

    private String bankName;

    // =========================================================
    // CARD CLASSIFICATION
    // =========================================================

    private CardNetwork network;

    private CardType cardType;

    private RewardType rewardType;

    private CardLevel cardLevel;

    private RewardValueType rewardValueType;

    // =========================================================
    // CARD FEES
    // =========================================================

    private Double joiningFee;

    private Double annualFee;

    private Double forexMarkup;

    // =========================================================
    // REWARD DETAILS
    // =========================================================

    private Double baseRewardValue;

    private Double rewardPointConversion;

    private Integer rewardPointExpiryMonths;

    // =========================================================
    // CARD FEATURES
    // =========================================================

    private Boolean emiAvailable;

    private Boolean fuelSurchargeWaiver;

    private Boolean airportLoungeAccess;

    private Boolean railwayLoungeAccess;

    private Boolean addOnCardAvailable;

    private Boolean contactlessEnabled;

    // =========================================================
    // LOUNGE ACCESS DETAILS
    // =========================================================

    private Integer domesticLoungeAccess;

    private Integer internationalLoungeAccess;

    private BenefitPeriod domesticLoungePeriod;

    private BenefitPeriod internationalLoungePeriod;

    // =========================================================
    // CO-BRANDED DETAILS
    // =========================================================

    private Boolean coBranded;

    private String coBrandPartner;

    // =========================================================
    // Digital & Network Flags
    // =========================================================

    private Boolean upiEnabled ;
    private Boolean isVirtualOnly ;
    private Boolean requiresPrimaryCard;


    // =========================================================
    // ELIGIBILITY
    // =========================================================

    private Integer minimumIncomeRequired;
}