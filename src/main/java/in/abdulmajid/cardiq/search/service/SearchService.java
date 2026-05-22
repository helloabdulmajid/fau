package in.abdulmajid.cardiq.search.service;

import in.abdulmajid.cardiq.benefit.enums.BenefitType;
import in.abdulmajid.cardiq.exception.ResourceNotFoundException;
import in.abdulmajid.cardiq.offer.entity.Offer;
import in.abdulmajid.cardiq.offer.repository.OfferRepository;
import in.abdulmajid.cardiq.search.dto.SearchCardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

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


                            /*
                             * Validate keyword matching
                             *
                             * All keywords must match
                             * somewhere
                             */

                            for (String word : keywords) {

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
                                        offer.getMerchant() != null &&
                                        offer.getMerchant()
                                                .getName()
                                                .toLowerCase()
                                                .contains(word)) {

                                    matched = true;

                                    relevanceScore += 40;
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
                                 * If keyword not matched,
                                 * reject this offer
                                 */

                                if (!matched) {

                                    return null;
                                }
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