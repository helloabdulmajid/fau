package in.abdulmajid.cardiq.search.service;
import in.abdulmajid.cardiq.common.enums.BenefitPeriod;

import in.abdulmajid.cardiq.benefit.enums.BenefitType;


import in.abdulmajid.cardiq.exception.ResourceNotFoundException;

import in.abdulmajid.cardiq.offer.entity.Offer;
import in.abdulmajid.cardiq.offer.repository.OfferRepository;

import in.abdulmajid.cardiq.search.dto.SearchCardResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static in.abdulmajid.cardiq.benefit.enums.BenefitType.REAL_CASHBACK;
import static in.abdulmajid.cardiq.offer.enums.OfferType.LOUNGE_ACCESS;

@Service
@RequiredArgsConstructor
public class SearchService {

    // =========================================================
    // REPOSITORIES
    // =========================================================

    private final OfferRepository offerRepository;

    // =========================================================
    // KEYWORD NORMALIZATION
    // =========================================================

    private static final Map<String, String>
            NORMALIZED_KEYWORDS = Map.ofEntries(

            Map.entry("travl", "travel"),
            Map.entry("travels", "travel"),
            Map.entry("traveling", "travel"),
            Map.entry("travelling", "travel"),

            Map.entry("amazn", "amazon"),
            Map.entry("amzn", "amazon"),

            Map.entry("flpkrt", "flipkart"),
            Map.entry("flipkrt", "flipkart"),
            Map.entry("flepkrt", "flipkart"),

            Map.entry("swigy", "swiggy"),
            Map.entry("swegy", "swiggy"),

            Map.entry("zomoto", "zomato"),
            Map.entry("zoomat", "zomato"),

            Map.entry("shoping", "shopping"),
            Map.entry("shooping", "shopping"),

            Map.entry("fods", "food"),
            Map.entry("fod", "food")
    );

    // =========================================================
    // SEARCH
    // =========================================================

    public List<SearchCardResponse> search(
            String keyword,
            Double amount
    ) {

        // -----------------------------------------------------
        // VALIDATE KEYWORD
        // -----------------------------------------------------

        if (keyword == null || keyword.isBlank()) {

            throw new ResourceNotFoundException(
                    "Search keyword is required"
            );
        }

        // -----------------------------------------------------
        // SPLIT KEYWORDS
        // -----------------------------------------------------

        List<String> keywords =
                List.of(
                        keyword
                                .toLowerCase()
                                .trim()
                                .split("\\s+")
                );

        // -----------------------------------------------------
        // FETCH ACTIVE OFFERS
        // -----------------------------------------------------

        List<SearchCardResponse> results =

                offerRepository
                        .findByActiveTrue()
                        .stream()

                        .map(offer -> {

                            int relevanceScore = 0;

                            List<String> matchedKeywords =
                                    new ArrayList<>();

                            List<String> unmatchedKeywords =
                                    new ArrayList<>();

                            List<String> suggestedKeywords =
                                    new ArrayList<>();

                            boolean exactMatch = true;

                            // =====================================
                            // KEYWORD MATCHING
                            // =====================================

                            for (String originalWord : keywords) {

                                String word =
                                        NORMALIZED_KEYWORDS
                                                .getOrDefault(
                                                        originalWord,
                                                        originalWord
                                                );

                                // ---------------------------------
                                // TYPO SUGGESTION
                                // ---------------------------------

                                if (!word.equals(originalWord)) {

                                    exactMatch = false;

                                    if (!suggestedKeywords.contains(word)) {

                                        suggestedKeywords.add(word);
                                    }
                                }

                                boolean matched = false;

                                // ---------------------------------
                                // OFFER TITLE MATCH
                                // ---------------------------------

                                if (
                                        offer.getTitle() != null &&
                                                offer.getTitle()
                                                        .toLowerCase()
                                                        .contains(word)
                                ) {

                                    matched = true;

                                    relevanceScore += 35;
                                }

                                // ---------------------------------
                                // MERCHANT MATCH
                                // ---------------------------------

                                if (
                                        !matched &&
                                                offer.getMerchant() != null &&
                                                offer.getMerchant()
                                                        .getName()
                                                        .toLowerCase()
                                                        .contains(word)
                                ) {

                                    matched = true;

                                    relevanceScore += 40;
                                }

                                // ---------------------------------
                                // CATEGORY MATCH
                                // ---------------------------------

                                if (
                                        !matched &&
                                                offer.getCategory() != null &&
                                                offer.getCategory()
                                                        .getName()
                                                        .toLowerCase()
                                                        .contains(word)
                                ) {

                                    matched = true;

                                    relevanceScore += 25;
                                }

                                // ---------------------------------
                                // BANK MATCH
                                // ---------------------------------

                                if (
                                        !matched &&
                                                offer.getCard() != null &&
                                                offer.getCard()
                                                        .getBank()
                                                        .getName()
                                                        .toLowerCase()
                                                        .contains(word)

                                ) {

                                    matched = true;

                                    relevanceScore += 20;
                                }

                                // ---------------------------------
                                // NETWORK MATCH
                                // ---------------------------------

                                if (
                                        !matched &&
                                                offer.getCard() != null &&
                                                offer.getCard()
                                                        .getNetwork()
                                                        .name()
                                                        .toLowerCase()
                                                        .contains(word)
                                ) {

                                    matched = true;

                                    relevanceScore += 15;
                                }

                                // ---------------------------------
                                // REWARD TYPE MATCH
                                // ---------------------------------

                                if (
                                        !matched &&
                                                offer.getCard()
                                                        .getRewardType()
                                                        .name()
                                                        .toLowerCase()
                                                        .contains(word)
                                ) {

                                    matched = true;

                                    relevanceScore += 20;
                                }

                                // ---------------------------------
                                // BENEFIT TYPE MATCH
                                // ---------------------------------

                                if (
                                        !matched &&
                                                offer.getBenefitRule() != null &&
                                                offer.getBenefitRule()
                                                        .getBenefitType()
                                                        .name()
                                                        .toLowerCase()
                                                        .contains(word)
                                ) {

                                    matched = true;

                                    relevanceScore += 25;
                                }

                                // ---------------------------------
                                // PLATFORM MATCH
                                // ---------------------------------

                                if (
                                        !matched &&
                                                offer.getPlatform() != null &&
                                                offer.getPlatform()
                                                        .name()
                                                        .toLowerCase()
                                                        .contains(word)
                                ) {

                                    matched = true;

                                    relevanceScore += 20;
                                }

                                // ---------------------------------
                                // STORE MATCH RESULT
                                // ---------------------------------

                                if (matched) {

                                    matchedKeywords.add(word);

                                } else {

                                    unmatchedKeywords.add(word);
                                }
                            }

                            // =====================================
                            // REJECT NON-MATCHED RESULTS
                            // =====================================

                            if (matchedKeywords.isEmpty()) {

                                return null;
                            }

                            // =====================================
                            // BENEFIT SCORING
                            // =====================================

                            int recommendationScore = 10;

                            BenefitType benefitType = null;

                            String benefitSummary =
                                    "Benefit details unavailable";

                            if (offer.getBenefitRule() != null) {

                                benefitType =
                                        offer.getBenefitRule()
                                                .getBenefitType();

                                recommendationScore =
                                        getBenefitTypeWeight(
                                                benefitType
                                        );

                                benefitSummary =
                                        buildBenefitSummary(
                                                offer
                                        );
                            }

                            // =====================================
                            // BOOST SCORES
                            // =====================================

                            if (
                                    Boolean.TRUE.equals(
                                            offer.getPermanentOffer()
                                    )
                            ) {

                                recommendationScore += 10;
                            }

                            if (
                                    Boolean.TRUE.equals(
                                            offer.getLimitedTimeOffer()
                                    )
                            ) {

                                recommendationScore += 5;
                            }

                            recommendationScore +=
                                    offer.getPriority();

                            relevanceScore +=
                                    matchedKeywords.size() * 15;

                            // =====================================
                            // MATCH PERCENTAGE
                            // =====================================

                            double matchPercentage =
                                    Math.round(
                                            (
                                                    (double)
                                                            matchedKeywords.size()
                                                            /
                                                            keywords.size()
                                            ) * 100
                                    );

                            // =====================================
                            // SEARCH MESSAGE
                            // =====================================

                            String searchMessage = null;

                            if (!suggestedKeywords.isEmpty()) {

                                searchMessage =
                                        "Showing results related to " +
                                                String.join(
                                                        ", ",
                                                        suggestedKeywords
                                                );
                            }

                            // =====================================
                            // FINAL SCORE
                            // =====================================

                            int finalScore =
                                    recommendationScore +
                                            relevanceScore;

                            // =====================================
                            // ESTIMATED SAVINGS
                            // =====================================

                            Double estimatedSavings = null;

                            if (amount != null) {

                                estimatedSavings =
                                        (amount * offer.getValue())
                                                / 100;
                            }

                            // =====================================
                            // BUILD RESPONSE
                            // =====================================

                            return SearchCardResponse
                                    .builder()

                                    // -----------------------------
                                    // CARD DETAILS
                                    // -----------------------------

                                    .cardName(
                                            offer.getCard().getName()
                                    )
                                    .cardSlung(
                                            offer.getCard().getSlug()
                                    )

                                    .bankName(
                                            offer.getCard()
                                                    .getBank()
                                                    .getName()
                                    )

                                    .imageUrl(
                                            offer.getCard()
                                                    .getImageUrl()
                                    )

                                    .network(
                                            offer.getCard()
                                                    .getNetwork()
                                                    .name()
                                    )

                                    .rewardType(
                                            offer.getCard()
                                                    .getRewardType()
                                    )

                                    .cardLevel(
                                            offer.getCard()
                                                    .getCardLevel()
                                                    .name()
                                    )

                                    .ltf(
                                            offer.getCard()
                                                    .getLtf()
                                    )

                                    .annualFee(
                                            offer.getCard()
                                                    .getAnnualFee()
                                    )

                                    .joiningFee(
                                            offer.getCard()
                                                    .getJoiningFee()
                                    )

                                    .forexMarkup(
                                            offer.getCard()
                                                    .getForexMarkup()
                                    )

                                    .airportLoungeAccess(
                                            offer.getCard()
                                                    .getAirportLoungeAccess()
                                    )

                                    .domesticLoungeAccess(
                                            offer.getCard()
                                                    .getDomesticLoungeAccess()
                                    )

                                    .internationalLoungeAccess(
                                            offer.getCard()
                                                    .getInternationalLoungeAccess()
                                    )

                                    .coBrandPartner(
                                            offer.getCard()
                                                    .getCoBrandPartner()
                                    )

                                    // -----------------------------
                                    // OFFER DETAILS
                                    // -----------------------------

                                    .offerTitle(
                                            offer.getTitle()
                                    )

                                    .value(
                                            offer.getValue()
                                    )

                                    .offerType(
                                            offer.getOfferType()
                                    )

                                    .merchantName(
                                            offer.getMerchant()
                                                    .getName()
                                    )

                                    .categoryName(
                                            offer.getCategory()
                                                    .getName()
                                    )

                                    .platform(
                                            offer.getPlatform()
                                    )

                                    .permanentOffer(
                                            offer.getPermanentOffer()
                                    )

                                    .limitedTimeOffer(
                                            offer.getLimitedTimeOffer()
                                    )

                                    .priority(
                                            offer.getPriority()
                                    )

                                    // -----------------------------
                                    // BENEFIT DETAILS
                                    // -----------------------------

                                    .benefitType(
                                            benefitType
                                    )

                                    .benefitRuleName(
                                            offer.getBenefitRule()
                                                    .getName()
                                    )

                                    .benefitSummary(
                                            benefitSummary
                                    )

                                    .cashbackCap(
                                            offer.getCashbackCap()
                                    )

                                    .minimumSpend(
                                            offer.getMinimumSpend()
                                    )

                                    .maxBenefit(
                                            offer.getMaxBenefit()
                                    )

                                    .estimatedSavings(
                                            estimatedSavings
                                    )

                                    // -----------------------------
                                    // SEARCH ANALYTICS
                                    // -----------------------------

                                    .recommendationScore(
                                            finalScore
                                    )

                                    .matchedKeywords(
                                            matchedKeywords
                                    )

                                    .unmatchedKeywords(
                                            unmatchedKeywords
                                    )

                                    .matchedKeywordCount(
                                            matchedKeywords.size()
                                    )

                                    .matchPercentage(
                                            matchPercentage
                                    )

                                    .suggestedKeywords(
                                            suggestedKeywords
                                    )

                                    .searchMessage(
                                            searchMessage
                                    )

                                    .exactMatch(
                                            exactMatch
                                    )

                                    .build();
                        })

                        // -----------------------------------------
                        // REMOVE NULL RESULTS
                        // -----------------------------------------

                        .filter(response -> response != null)

                        // -----------------------------------------
                        // SORT BEST RESULTS FIRST
                        // -----------------------------------------

                        .sorted(
                                Comparator.comparing(
                                        SearchCardResponse
                                                ::getRecommendationScore
                                ).reversed()
                        )

                        .toList();

        // -----------------------------------------------------
        // RETURN RESULTS
        // -----------------------------------------------------

        return results;
    }

    // =========================================================
// BUILD BENEFIT SUMMARY
// =========================================================

    private int getBenefitTypeWeight(
            BenefitType benefitType
    ) {

        return switch (benefitType) {
            case INSTANT_DISCOUNT->100;


            case STATEMENT_CREDIT -> 99;

            case REAL_CASHBACK->98;

            case WALLET_BALANCE -> 95;

            case REWARD_POINTS -> 80;

            case AIRLINE_MILES -> 75;

            case HOTEL_POINTS -> 70;

            case VOUCHER -> 65;

            case CASHPOINTS -> 60;

            case EDGE_REWARDS -> 55;

            case MILESTONE_REWARD -> 50;
        };
    }

        // -----------------------------------------------------
        // BENEFIT TYPE SUMMARY
        // -----------------------------------------------------

    private String buildBenefitSummary(
            Offer offer
    ) {

        if (
                offer.getBenefitRule() == null
                        ||
                        offer.getBenefitRule().getBenefitType() == null
        ) {

            return "Card benefit available";
        }

        String value =
                offer.getValue() != null
                        ? offer.getValue() + "%"
                        : "";

        return switch (
                offer.getBenefitRule()
                        .getBenefitType()
                ) {
            case REAL_CASHBACK ->

                    value +
                            " REAL_CASHBACK You Get to Your Card Limits";

            case INSTANT_DISCOUNT ->

                    value +
                            " INSTANT_DISCOUNT You Get real-time point-of-sale or Online ";

            case STATEMENT_CREDIT ->

                    value +
                            " cashback credited to statement";

            case WALLET_BALANCE ->

                    value +
                            " cashback credited to wallet balance";

            case REWARD_POINTS ->

                    value +
                            " reward points benefit";

            case AIRLINE_MILES ->

                    value +
                            " airline miles reward";

            case HOTEL_POINTS ->

                    value +
                            " hotel loyalty points";

            case VOUCHER ->

                    value +
                            " voucher reward benefit";

            case CASHPOINTS ->

                    value +
                            " cashpoints reward";

            case EDGE_REWARDS ->

                    value +
                            " edge rewards benefit";

            case MILESTONE_REWARD ->

                    "Milestone reward available";

            default -> "Card benefit available";
        };
    }
}