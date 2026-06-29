package in.abdulmajid.fau.card.dto;

import in.abdulmajid.fau.card.enums.CardNetwork;
import in.abdulmajid.fau.card.enums.CardType;

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
