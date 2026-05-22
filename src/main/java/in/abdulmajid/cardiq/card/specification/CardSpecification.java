package in.abdulmajid.cardiq.card.specification;

import in.abdulmajid.cardiq.card.dto.CardFilterRequest;
import in.abdulmajid.cardiq.card.entity.Card;
import org.springframework.data.jpa.domain.Specification;

public class CardSpecification {

    public static Specification<Card> filterCards(
            CardFilterRequest filter
    ) {

        return (root, query, criteriaBuilder) -> {

            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            if (filter.getLtf() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("ltf"),
                                filter.getLtf()
                        )
                );
            }

            if (filter.getAirportLoungeAccess() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("airportLoungeAccess"),
                                filter.getAirportLoungeAccess()
                        )
                );
            }

            if (filter.getRailwayLoungeAccess() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("railwayLoungeAccess"),
                                filter.getRailwayLoungeAccess()
                        )
                );
            }

            if (filter.getFuelSurchargeWaiver() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("fuelSurchargeWaiver"),
                                filter.getFuelSurchargeWaiver()
                        )
                );
            }

            if (filter.getNetwork() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("network"),
                                filter.getNetwork()
                        )
                );
            }

            if (filter.getCardType() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("cardType"),
                                filter.getCardType()
                        )
                );
            }

            if (filter.getRewardType() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("rewardType"),
                                filter.getRewardType()
                        )
                );
            }

            if (filter.getMaxAnnualFee() != null) {

                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("annualFee"),
                                filter.getMaxAnnualFee()
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(
                            new jakarta.persistence.criteria.Predicate[0]
                    )
            );
        };
    }
}