package in.abdulmajid.cardiq.search.service;

import in.abdulmajid.cardiq.benefit.enums.BenefitType;
import in.abdulmajid.cardiq.common.enums.BenefitPeriod;
import in.abdulmajid.cardiq.exception.ResourceNotFoundException;
import in.abdulmajid.cardiq.offer.entity.Offer;
import in.abdulmajid.cardiq.offer.repository.OfferRepository;
import in.abdulmajid.cardiq.search.dto.SearchCardResponse;
import in.abdulmajid.cardiq.usercard.repository.UserCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final OfferRepository offerRepository;
    private final UserCardRepository userCardRepository;

    private static final Map<String, String> NORMALIZED_KEYWORDS = Map.ofEntries(
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

    public List<SearchCardResponse> search(
            String keyword,
            Double amount,
            String mode,
            Long userId
    ) {
        if (keyword == null || keyword.isBlank()) {
            throw new ResourceNotFoundException("Search keyword is required");
        }

        List<String> keywords = List.of(
                keyword.toLowerCase().trim().split("\\s+")
        );

        List<Offer> validOffers = offerRepository.findActiveValidOffers(LocalDate.now());

        Set<Long> myCardIds = ("me".equals(mode) && userId != null)
                ? userCardRepository.findByUserId(userId)
                    .stream()
                    .map(uc -> uc.getCard().getId())
                    .collect(Collectors.toSet())
                : Collections.emptySet();

        List<SearchCardResponse> results = validOffers.stream()
                .filter(offer -> offer.getCard() != null
                        && offer.getMerchant() != null
                        && offer.getCategory() != null)
                .map(offer -> buildResult(offer, keywords, amount, myCardIds))
                .filter(Objects::nonNull)
                .sorted((a, b) -> {
                    if (a.isOwned() != b.isOwned()) {
                        return a.isOwned() ? -1 : 1;
                    }
                    return Integer.compare(b.getRecommendationScore(), a.getRecommendationScore());
                })
                .toList();

        return results;
    }

    private SearchCardResponse buildResult(
            Offer offer,
            List<String> keywords,
            Double amount,
            Set<Long> myCardIds
    ) {
        int relevanceScore = 0;
        List<String> matchedKeywords = new ArrayList<>();
        List<String> unmatchedKeywords = new ArrayList<>();
        List<String> suggestedKeywords = new ArrayList<>();
        boolean exactMatch = true;

        for (String originalWord : keywords) {
            String word = NORMALIZED_KEYWORDS.getOrDefault(originalWord, originalWord);

            if (!word.equals(originalWord)) {
                exactMatch = false;
                if (!suggestedKeywords.contains(word)) {
                    suggestedKeywords.add(word);
                }
            }

            int wordScore = 0;

            wordScore += matchField(offer.getTitle(), word, 35);
            wordScore += matchField(offer.getMerchant() != null ? offer.getMerchant().getName() : null, word, 40);
            wordScore += matchField(offer.getCategory() != null ? offer.getCategory().getName() : null, word, 25);
            wordScore += matchField(offer.getCard() != null && offer.getCard().getBank() != null
                    ? offer.getCard().getBank().getName() : null, word, 20);
            wordScore += matchField(offer.getCard() != null ? offer.getCard().getNetwork().name() : null, word, 15);
            wordScore += matchField(offer.getCard() != null ? offer.getCard().getRewardType().name() : null, word, 20);
            wordScore += matchField(offer.getBenefitRule() != null ? offer.getBenefitRule().getBenefitType().name() : null, word, 25);
            wordScore += matchField(offer.getPlatform() != null ? offer.getPlatform().name() : null, word, 20);
            // Card name — high weight so "Axis Flipkart" matches "Axis Flipkart Credit Card" (issue #6)
            wordScore += matchField(offer.getCard() != null ? offer.getCard().getName() : null, word, 30);
            // Merchant slug for alternate lookups (e.g. "flipkart" vs slug "flipkart")
            wordScore += matchField(offer.getMerchant() != null ? offer.getMerchant().getSlug() : null, word, 15);

            if (wordScore > 0) {
                relevanceScore += wordScore;
                matchedKeywords.add(word);
            } else {
                unmatchedKeywords.add(word);
            }
        }

        if (matchedKeywords.isEmpty()) {
            return null;
        }

        int recommendationScore = 10;
        BenefitType benefitType = null;
        String benefitSummary = "Benefit details unavailable";

        if (offer.getBenefitRule() != null) {
            benefitType = offer.getBenefitRule().getBenefitType();
            recommendationScore = getBenefitTypeWeight(benefitType);
            benefitSummary = buildBenefitSummary(offer);
        }

        if (Boolean.TRUE.equals(offer.getPermanentOffer())) {
            recommendationScore += 10;
        }

        if (Boolean.TRUE.equals(offer.getLimitedTimeOffer())) {
            recommendationScore += 5;
        }

        recommendationScore += offer.getPriority() != null ? offer.getPriority() : 0;

        relevanceScore += matchedKeywords.size() * 15;

        double matchPercentage = Math.round(
                (double) matchedKeywords.size() / keywords.size() * 100
        );

        String searchMessage = null;
        if (!suggestedKeywords.isEmpty()) {
            searchMessage = "Showing results related to " +
                    String.join(", ", suggestedKeywords);
        }

        int finalScore = recommendationScore + relevanceScore;

        boolean owned = myCardIds.contains(offer.getCard().getId());

        if (owned) {
            finalScore += 200;
        }

        Double estimatedSavings = null;
        if (amount != null && offer.getValue() != null) {
            estimatedSavings = (amount * offer.getValue()) / 100;
        }

        return SearchCardResponse.builder()
                .id(offer.getCard().getId())
                .cardName(offer.getCard().getName())
                .cardSlug(offer.getCard().getSlug())
                .bankName(offer.getCard().getBank().getName())
                .imageUrl(offer.getCard().getImageUrl())
                .network(offer.getCard().getNetwork().name())
                .rewardType(offer.getCard().getRewardType())
                .cardLevel(offer.getCard().getCardLevel().name())
                .ltf(offer.getCard().getLtf())
                .annualFee(offer.getCard().getAnnualFee())
                .joiningFee(offer.getCard().getJoiningFee())
                .forexMarkup(offer.getCard().getForexMarkup())
                .airportLoungeAccess(offer.getCard().getAirportLoungeAccess())
                .domesticLoungeAccess(offer.getCard().getDomesticLoungeAccess())
                .internationalLoungeAccess(offer.getCard().getInternationalLoungeAccess())
                .coBrandPartner(offer.getCard().getCoBrandPartner())
                .cardType(offer.getCard().getCardType())
                .offerTitle(offer.getTitle())
                .value(offer.getValue())
                .offerType(offer.getOfferType())
                .merchantName(offer.getMerchant().getName())
                .categoryName(offer.getCategory().getName())
                .platform(offer.getPlatform())
                .permanentOffer(offer.getPermanentOffer())
                .limitedTimeOffer(offer.getLimitedTimeOffer())
                .priority(offer.getPriority())
                .benefitType(benefitType)
                .benefitRuleName(offer.getBenefitRule() != null ? offer.getBenefitRule().getName() : null)
                .benefitSummary(benefitSummary)
                .cashbackCap(offer.getCashbackCap())
                .minimumSpend(offer.getMinimumSpend())
                .maxBenefit(offer.getMaxBenefit())
                .estimatedSavings(estimatedSavings)
                .recommendationScore(finalScore)
                .matchedKeywords(matchedKeywords)
                .unmatchedKeywords(unmatchedKeywords)
                .matchedKeywordCount(matchedKeywords.size())
                .matchPercentage(matchPercentage)
                .suggestedKeywords(suggestedKeywords)
                .searchMessage(searchMessage)
                .exactMatch(exactMatch)
                .owned(owned)
                .build();
    }

    private int matchField(String fieldValue, String word, int weight) {
        if (fieldValue == null) return 0;
        if (fieldValue.toLowerCase().contains(word)) return weight;
        return 0;
    }

    private int getBenefitTypeWeight(BenefitType benefitType) {
        return switch (benefitType) {
            case INSTANT_DISCOUNT -> 100;
            case STATEMENT_CREDIT -> 99;
            case REAL_CASHBACK -> 98;
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

    private String buildBenefitSummary(Offer offer) {
        if (offer.getBenefitRule() == null || offer.getBenefitRule().getBenefitType() == null) {
            return "Card benefit available";
        }

        String value = offer.getValue() != null ? offer.getValue() + "%" : "";

        return switch (offer.getBenefitRule().getBenefitType()) {
            case REAL_CASHBACK -> value + " cashback credited directly to your account";
            case INSTANT_DISCOUNT -> value + " instant discount applied at checkout";
            case STATEMENT_CREDIT -> value + " cashback credited to your statement";
            case WALLET_BALANCE -> value + " cashback credited to wallet balance";
            case REWARD_POINTS -> value + " reward points on every spend";
            case AIRLINE_MILES -> value + " airline miles per transaction";
            case HOTEL_POINTS -> value + " hotel loyalty points earned";
            case VOUCHER -> value + " voucher reward on qualifying spend";
            case CASHPOINTS -> value + " CashPoints earned per transaction";
            case EDGE_REWARDS -> value + " Edge Reward points per transaction";
            case MILESTONE_REWARD -> "Milestone reward on reaching spend target";
            default -> "Card benefit available";
        };
    }
}
