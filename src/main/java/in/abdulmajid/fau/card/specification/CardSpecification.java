package in.abdulmajid.fau.card.specification;

import in.abdulmajid.fau.card.dto.CardFilterRequest;
import in.abdulmajid.fau.card.entity.Card;

import org.springframework.data.jpa.domain.Specification;

public class CardSpecification {

    // =========================================================
    // DYNAMIC CARD FILTER
    // =========================================================

    public static Specification<Card> filterCards(
            CardFilterRequest filter
    ) {

        return (root, query, criteriaBuilder) -> {

            // -------------------------------------------------
            // STORE ALL FILTER CONDITIONS
            // -------------------------------------------------

            var predicates =
                    new java.util.ArrayList<
                            jakarta.persistence.criteria.Predicate
                            >();

            // -------------------------------------------------
            // LTF FILTER
            // -------------------------------------------------

            if (filter.getLtf() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("ltf"),
                                filter.getLtf()
                        )
                );
            }

            // -------------------------------------------------
            // AIRPORT LOUNGE FILTER
            // -------------------------------------------------

            if (filter.getAirportLoungeAccess() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("airportLoungeAccess"),
                                filter.getAirportLoungeAccess()
                        )
                );
            }

            // -------------------------------------------------
            // RAILWAY LOUNGE FILTER
            // -------------------------------------------------

            if (filter.getRailwayLoungeAccess() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("railwayLoungeAccess"),
                                filter.getRailwayLoungeAccess()
                        )
                );
            }

            // -------------------------------------------------
            // FUEL SURCHARGE FILTER
            // -------------------------------------------------

            if (filter.getFuelSurchargeWaiver() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("fuelSurchargeWaiver"),
                                filter.getFuelSurchargeWaiver()
                        )
                );
            }

            // -------------------------------------------------
            // CO-BRANDED FILTER
            // -------------------------------------------------

            if (filter.getCoBranded() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("coBranded"),
                                filter.getCoBranded()
                        )
                );
            }

            // -------------------------------------------------
            // CARD NETWORK FILTER
            // -------------------------------------------------

            if (filter.getNetwork() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("network"),
                                filter.getNetwork()
                        )
                );
            }

            // -------------------------------------------------
            // CARD TYPE FILTER
            // -------------------------------------------------

            if (filter.getCardType() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("cardType"),
                                filter.getCardType()
                        )
                );
            }

            // -------------------------------------------------
            // REWARD TYPE FILTER
            // -------------------------------------------------

            if (filter.getRewardType() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("rewardType"),
                                filter.getRewardType()
                        )
                );
            }

            // -------------------------------------------------
            // CARD LEVEL FILTER
            // -------------------------------------------------

            if (filter.getCardLevel() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("cardLevel"),
                                filter.getCardLevel()
                        )
                );
            }

            // -------------------------------------------------
            // MAX ANNUAL FEE FILTER
            // -------------------------------------------------

            if (filter.getMaxAnnualFee() != null) {

                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("annualFee"),
                                filter.getMaxAnnualFee()
                        )
                );
            }

            // -------------------------------------------------
            // MAX FOREX MARKUP FILTER
            // -------------------------------------------------

            if (filter.getMaxForexMarkup() != null) {

                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("forexMarkup"),
                                filter.getMaxForexMarkup()
                        )
                );
            }

            // -------------------------------------------------
            // RETURN FINAL FILTER QUERY
            // -------------------------------------------------

            return criteriaBuilder.and(
                    predicates.toArray(
                            new jakarta.persistence.criteria.Predicate[0]
                    )
            );
        };
    }
}