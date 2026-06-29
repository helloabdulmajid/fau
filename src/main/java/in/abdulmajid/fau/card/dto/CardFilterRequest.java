package in.abdulmajid.fau.card.dto;

import in.abdulmajid.fau.card.enums.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardFilterRequest {

    // =========================================================
    // CARD FEATURES
    // =========================================================

    private Boolean ltf;

    private Boolean airportLoungeAccess;

    private Boolean railwayLoungeAccess;

    private Boolean fuelSurchargeWaiver;

    private Boolean coBranded;

    // =========================================================
    // CARD CLASSIFICATION
    // =========================================================

    private CardNetwork network;

    private CardType cardType;

    private RewardType rewardType;

    private CardLevel cardLevel;

    // =========================================================
    // CARD FEES
    // =========================================================

    private Double maxAnnualFee;

    private Double maxForexMarkup;
}