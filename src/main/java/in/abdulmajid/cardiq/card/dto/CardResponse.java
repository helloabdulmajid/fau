package in.abdulmajid.cardiq.card.dto;

import in.abdulmajid.cardiq.card.enums.CardLevel;
import in.abdulmajid.cardiq.card.enums.CardNetwork;
import in.abdulmajid.cardiq.card.enums.CardType;
import in.abdulmajid.cardiq.card.enums.RewardType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CardResponse {

    private Long id;

    private String name;

    private String bankName;

    private CardNetwork network;

    private CardType cardType;

    private RewardType rewardType;

    private CardLevel cardLevel;

    private Double joiningFee;

    private Double annualFee;

    private Boolean ltf;

    private Boolean airportLoungeAccess;

    private Boolean railwayLoungeAccess;

    private Boolean fuelSurchargeWaiver;

    private Boolean coBranded;

    private Boolean emiAvailable;

    private Integer rewardRate;

    private String description;

    private Boolean active;

    private String coBrandPartner;

    private String imageUrl;
}