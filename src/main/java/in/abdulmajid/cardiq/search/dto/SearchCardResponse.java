package in.abdulmajid.cardiq.search.dto;

import in.abdulmajid.cardiq.benefit.enums.BenefitType;
import in.abdulmajid.cardiq.card.enums.RewardType;
import in.abdulmajid.cardiq.offer.enums.OfferPlatform;
import in.abdulmajid.cardiq.offer.enums.OfferType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchCardResponse {

    // =========================================================
    // CARD DETAILS
    // =========================================================

    private String cardName;
    private String cardSlung;


    private String bankName;

    private String imageUrl;

    private String network;

    private RewardType rewardType;

    private String cardLevel;

    private Boolean ltf;

    private Double annualFee;

    private Double joiningFee;

    private Double forexMarkup;

    private Boolean airportLoungeAccess;

    private Integer domesticLoungeAccess;

    private Integer internationalLoungeAccess;

    private String coBrandPartner;

    // =========================================================
    // OFFER DETAILS
    // =========================================================

    private String offerTitle;

    private Double value;

    private OfferType offerType;

    private String merchantName;

    private String categoryName;

    private OfferPlatform platform;

    private Boolean permanentOffer;

    private Boolean limitedTimeOffer;

    private Integer priority;

    // =========================================================
    // BENEFIT DETAILS
    // =========================================================

    private BenefitType benefitType;

    private String benefitRuleName;

    private String benefitSummary;

    private Double cashbackCap;

    private Double minimumSpend;

    private Double maxBenefit;

    private Double estimatedSavings;

    // =========================================================
    // SEARCH ANALYTICS
    // =========================================================

    private Integer recommendationScore;

    private List<String> matchedKeywords;

    private List<String> unmatchedKeywords;

    private Integer matchedKeywordCount;

    private Double matchPercentage;

    private List<String> suggestedKeywords;

    private String searchMessage;

    private Boolean exactMatch;
}