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

        if (keyword == null || keyword.isBlank()) {

            throw new ResourceNotFoundException(
                    "Search keyword is required"
            );
        }

        List<String> keywords =
                List.of(
                        keyword
                                .toLowerCase()
                                .split("\\s+")
                );

        List<SearchCardResponse> results =
                offerRepository
                        .findByActiveTrue()
                        .stream()

                        .filter(offer -> {

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
                                }


                                /*
                                 * Match category name
                                 */

                                if (!matched &&
                                        offer.getCategory() != null &&
                                        offer.getCategory()
                                                .getName()
                                                .toLowerCase()
                                                .contains(word)) {

                                    matched = true;
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
                                }


                                /*
                                 * Match card network
                                 */

                                if (!matched &&
                                        offer.getCard() != null &&
                                        offer.getCard()
                                                .getNetwork()
                                                .name()
                                                .toLowerCase()
                                                .contains(word)) {

                                    matched = true;
                                }


                                /*
                                 * Match reward type
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
                                }


                                /*
                                 * If one keyword does not match,
                                 * reject this offer
                                 */

                                if (!matched) {

                                    return false;
                                }
                            }

                            return true;
                        })

                        .map(offer -> {

                            /*
                             * Default values
                             * Used when BenefitRule is null
                             */

                            int recommendationScore = 10;

                            String benefitType =
                                    "UNKNOWN";

                            String benefitSummary =
                                    "Benefit details unavailable";


                            /*
                             * If BenefitRule exists,
                             * calculate recommendation data
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
                             * Calculate estimated savings
                             * only if amount is provided
                             */

                            Double estimatedSavings =
                                    null;

                            if (amount != null) {

                                estimatedSavings =
                                        (amount *
                                                offer.getValue()) / 100;
                            }


                            /*
                             * Build final response
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
                                            recommendationScore
                                    )

                                    .estimatedSavings(
                                            estimatedSavings
                                    )

                                    .build();
                        })

                        /*
                         * Sort best recommendations first
                         */

                        .sorted(
                                Comparator.comparing(
                                        SearchCardResponse
                                                ::getRecommendationScore
                                ).reversed()
                        )

                        .toList();


        /*
         * No search results found
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