package in.abdulmajid.cardiq.search.dto;

import in.abdulmajid.cardiq.benefit.enums.BenefitType;
import in.abdulmajid.cardiq.offer.enums.OfferPlatform;
import in.abdulmajid.cardiq.offer.enums.OfferType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchCardResponse {

    private String cardName;

    private String bankName;

    private String offerTitle;

    private Double value;

    private OfferType offerType;

    private String merchantName;

    private String categoryName;

    private OfferPlatform platform;
    private String imageUrl;

    private String network;

    private String rewardType;
    private Boolean permanentOffer;

    private Boolean limitedTimeOffer;

    private Integer priority;
    private String benefitType;

    private Integer recommendationScore;
    private String benefitRuleName;
    private String benefitSummary;
    private Double estimatedSavings;
    private List<String> matchedKeywords;

    private List<String> unmatchedKeywords;

    private Integer matchedKeywordCount;
    private Double matchPercentage;
    private List<String> suggestedKeywords;

    private Boolean fuzzyMatched;

    private String searchMessage;

}