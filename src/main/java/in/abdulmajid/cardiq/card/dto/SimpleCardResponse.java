package in.abdulmajid.cardiq.card.dto;

import in.abdulmajid.cardiq.card.enums.CardNetwork;
import in.abdulmajid.cardiq.card.enums.CardType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimpleCardResponse {

    private Long id;
    private String cardName;
    private String bankName;
    private String imageUrl;
    private CardType cardType;
    private CardNetwork network;
}
