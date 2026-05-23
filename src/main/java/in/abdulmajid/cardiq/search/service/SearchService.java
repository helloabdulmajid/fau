package in.abdulmajid.cardiq.search.service;

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

@Service
@RequiredArgsConstructor
public class SearchService {

    private final OfferRepository offerRepository;

    public List<SearchCardResponse> search(
            String keyword,
            Double amount
    ) {

        /*
         * Validate search keyword
         */

        if (keyword == null || keyword.isBlank()) {

            throw new ResourceNotFoundException(
                    "Search keyword is required"
            );
        }


        /*
         * Split keywords
         *
         * Example:
         * "amazon cashback"
         *
         * becomes:
         *
         * ["amazon", "cashback"]
         */

        List<String> keywords =
                List.of(
                        keyword
                                .toLowerCase()
                                .split("\\s+")
                );


        /*
         * Fetch all active offers
         */

        List<SearchCardResponse> results =
                offerRepository
                        .findByActiveTrue()
                        .stream()

                        .map(offer -> {

                            /*
                             * Relevance score
                             *
                             * Measures:
                             * how closely offer matches
                             * user search intent
                             */

                            int relevanceScore = 0;

                            List<String> matchedKeywords =
                                    new ArrayList<>();

                            List<String> unmatchedKeywords =
                                    new ArrayList<>();

                            List<String> suggestedKeywords =
                                    new ArrayList<>();

                            boolean exactMatch = true;




                            /*
                             * Validate keyword matching
                             *
                             * All keywords must match
                             * somewhere
                             */
                            /*
                             * Keyword normalization
                             * and typo suggestions
                             */

                            Map<String, String> normalizedKeywords =
                                    Map.ofEntries(

                                            Map.entry("travl", "travel"),
                                            Map.entry("travels", "travel"),
                                            Map.entry("traveling", "travel"),
                                            Map.entry("travelling", "travel"),

                                            Map.entry("amazn", "amazon"),
                                            Map.entry("amzn", "amazon"),
                                            Map.entry("flpkrt", "flipkart"),
                                            Map.entry("flepkrt", "flipkart"),
                                            Map.entry("flpcart", "flipkart"),

                                            Map.entry("flipkrt", "flipkart"),
                                            Map.entry("swigy", "swiggy"),
                                            Map.entry("swegy", "swiggy"),
                                            Map.entry("sweegy", "swiggy"),
                                            Map.entry("suegy", "swiggy"),
                                            Map.entry("eternal", "zomato"),
                                            Map.entry("zomoto", "zomato"),
                                            Map.entry("zoomat", "zomato"),

                                            Map.entry("zomat", "zomato"),

                                            Map.entry("foods", "food"),
                                            Map.entry("fods", "food"),
                                            Map.entry("fod", "food"),
                                            Map.entry("shoping", "shopping"),
                                            Map.entry("shooping", "shopping"),
                                            Map.entry("swoping", "shopping"),
                                            Map.entry("shoppingg", "shopping")
                                    );

                            for (String originalWord  : keywords)
                            {
                                String word =
                                        normalizedKeywords.getOrDefault(
                                                originalWord,
                                                originalWord
                                        );

                                /*
                                 * Add suggestion
                                 * only when corrected
                                 */

                                if (!word.equals(originalWord)) {
                                    exactMatch = false;

                                    if (!suggestedKeywords.contains(word)) {

                                        suggestedKeywords.add(word);
                                    }
                                }

                                boolean matched = false;


                                /*
                                 * Match offer title
                                 */

                                if (offer.getTitle() != null &&
                                        offer.getTitle()
                                                .toLowerCase()
                                                .contains(word)) {

                                    matched = true;

                                    relevanceScore += 35;
                                }

                                /*
                                 * Match merchant name
                                 */

                                if (!matched &&
                                        offer.getMerchant() != null) {

                                    String merchantName =
                                            offer.getMerchant()
                                                    .getName()
                                                    .toLowerCase();

                                    /*
                                     * Exact merchant match
                                     */

                                    if (merchantName.contains(word)) {

                                        matched = true;

                                        relevanceScore += 40;
                                    }

                                }

                                /*
                                 * Match merchant description
                                 */

                                if (!matched &&
                                        offer.getMerchant() != null &&
                                        offer.getMerchant()
                                                .getDescription() != null &&
                                        offer.getMerchant()
                                                .getDescription()
                                                .toLowerCase()
                                                .contains(word)) {

                                    matched = true;

                                    relevanceScore += 15;
                                }


                                /*
                                 * Match category
                                 */

                                if (!matched &&
                                        offer.getCategory() != null &&
                                        offer.getCategory()
                                                .getName()
                                                .toLowerCase()
                                                .contains(word)) {

                                    matched = true;

                                    relevanceScore += 20;
                                }


                                /*
                                 * Match bank name
                                 */

                                if (!matched &&
                                        offer.getCard() != null &&
                                        offer.getCard()
                                                .getBank()
                                                .getName()
                                                .toLowerCase()
                                                .contains(word)) {

                                    matched = true;

                                    relevanceScore += 15;
                                }


                                /*
                                 * Match card network
                                 */

                                if (!matched &&
                                        offer.getCard() != null &&
                                        offer.getCard()
                                                .getNetwork() != null &&
                                        offer.getCard()
                                                .getNetwork()
                                                .name()
                                                .toLowerCase()
                                                .contains(word)) {

                                    matched = true;

                                    relevanceScore += 15;
                                }


                                /*
                                 * Match reward type
                                 */

                                if (!matched &&
                                        offer.getCard() != null &&
                                        offer.getCard()
                                                .getRewardType() != null &&
                                        offer.getCard()
                                                .getRewardType()
                                                .name()
                                                .toLowerCase()
                                                .contains(word)) {

                                    matched = true;

                                    relevanceScore += 20;
                                }


                                /*
                                 * Match benefit type
                                 */

                                if (!matched &&
                                        offer.getBenefitRule() != null &&
                                        offer.getBenefitRule()
                                                .getBenefitType()
                                                .name()
                                                .toLowerCase()
                                                .contains(word)) {

                                    matched = true;

                                    relevanceScore += 25;
                                }

                                /*
                                 * Match offer platform
                                 */

                                if (!matched &&
                                        offer.getPlatform() != null &&
                                        offer.getPlatform()
                                                .name()
                                                .toLowerCase()
                                                .contains(word)) {

                                    matched = true;

                                    relevanceScore += 20;
                                }


                                /*
                                 * If keyword not matched or matched added keyword
                                 * not reject this offer
                                 */
                                if (matched) {

                                    if (!matchedKeywords.contains(word)) {

                                        matchedKeywords.add(word);
                                    }

                                } else {

                                    if (!unmatchedKeywords.contains(word)) {

                                        unmatchedKeywords.add(word);
                                    }
                                }

                            }

                            if (matchedKeywords.isEmpty()) {

                                return null;
                            }


                            /*
                             * Recommendation score
                             *
                             * Measures:
                             * actual usefulness/value
                             */

                            int recommendationScore = 10;

                            String benefitType =
                                    "UNKNOWN";

                            String benefitSummary =
                                    "Benefit details unavailable";


                            /*
                             * BenefitRule-based scoring
                             */

                            if (offer.getBenefitRule() != null) {

                                BenefitType currentBenefitType =
                                        offer.getBenefitRule()
                                                .getBenefitType();

                                benefitType =
                                        currentBenefitType.name();

                                recommendationScore =
                                        getBenefitTypeWeight(
                                                currentBenefitType
                                        );

                                benefitSummary =
                                        buildBenefitSummary(
                                                offer
                                        );
                            }


                            /*
                             * Boost permanent offers
                             */

                            if (Boolean.TRUE.equals(
                                    offer.getPermanentOffer()
                            )) {

                                recommendationScore += 10;
                            }


                            /*
                             * Boost limited-time offers
                             */

                            if (Boolean.TRUE.equals(
                                    offer.getLimitedTimeOffer()
                            )) {

                                recommendationScore += 5;
                            }


                            /*
                             * Add admin priority
                             */

                            recommendationScore +=
                                    offer.getPriority();

                            /*
                             * Boost offers with more
                             * matched keywords
                             */

                            relevanceScore +=
                                    matchedKeywords.size() * 15;

                            /*
                             * Calculate keyword match percentage
                             */

                            double matchPercentage =
                                    Math.round(
                                            ((double) matchedKeywords.size()
                                                    / keywords.size()) * 100
                                    );

                            String searchMessage = null;

                            if (!suggestedKeywords.isEmpty()) {

                                searchMessage =
                                        "Showing results related to  " +
                                                String.join(
                                                        ", ",
                                                        suggestedKeywords
                                                );
                            }


                            /*
                             * Final intelligent ranking
                             */

                            int finalScore =
                                    recommendationScore +
                                            relevanceScore;


                            /*
                             * Estimated savings
                             */

                            Double estimatedSavings =
                                    null;

                            if (amount != null) {

                                estimatedSavings =
                                        (amount *
                                                offer.getValue()) / 100;
                            }


                            /*
                             * Build response
                             */

                            return SearchCardResponse
                                    .builder()

                                    .cardName(
                                            offer.getCard()
                                                    .getName()
                                    )

                                    .bankName(
                                            offer.getCard()
                                                    .getBank()
                                                    .getName()
                                    )

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
                                                    .name()
                                    )

                                    .priority(
                                            offer.getPriority()
                                    )

                                    .benefitType(
                                            benefitType
                                    )

                                    .benefitSummary(
                                            benefitSummary
                                    )

                                    .recommendationScore(
                                            finalScore
                                    )

                                    .estimatedSavings(
                                            estimatedSavings
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

                        /*
                         * Remove rejected offers
                         */

                        .filter(response -> response != null)

                        /*
                         * Sort highest ranked first
                         */

                        .sorted(
                                Comparator.comparing(
                                        SearchCardResponse
                                                ::getRecommendationScore
                                ).reversed()
                        )

                        .toList();


        /*
         * No results found
         */

        if (results.isEmpty()) {

            throw new ResourceNotFoundException(
                    "No offers found"
            );
        }

        return results;
    }

    /*
     * Recommendation strength
     * based on real user value
     */

    private int getBenefitTypeWeight(
            BenefitType benefitType
    ) {

        return switch (benefitType) {

            case REAL_CASHBACK -> 100;

            case STATEMENT_CREDIT -> 95;

            case INSTANT_DISCOUNT -> 90;

            case WALLET_CASHBACK -> 80;

            case AIR_MILES -> 75;

            case VOUCHER -> 65;

            case REWARD_POINTS -> 55;

            case EMI_BENEFIT -> 40;

            case FUEL_WAIVER -> 35;

            case LOUNGE_ACCESS -> 30;

            default -> 10;
        };
    }


    /*
     * User-friendly explanation
     * of offer benefit
     */

    private String buildBenefitSummary(
            Offer offer
    ) {

        String value =
                offer.getValue() + "%";

        return switch (
                offer.getBenefitRule()
                        .getBenefitType()
                ) {

            case REAL_CASHBACK ->
                    value +
                            " real cashback credited to statement";

            case STATEMENT_CREDIT ->
                    value +
                            " statement credit benefit";

            case WALLET_CASHBACK ->
                    value +
                            " cashback credited to wallet";

            case INSTANT_DISCOUNT ->
                    value +
                            " instant discount on payment";

            case REWARD_POINTS ->
                    value +
                            " equivalent reward points benefit";

            case AIR_MILES ->
                    value +
                            " travel air miles rewards";

            case VOUCHER ->
                    value +
                            " voucher-based reward benefit";

            case EMI_BENEFIT ->
                    "No-cost EMI or EMI savings";

            case FUEL_WAIVER ->
                    "Fuel surcharge waiver available";

            case LOUNGE_ACCESS ->
                    "Complimentary lounge access";

            default ->
                    "Card benefit available";
        };
    }


}