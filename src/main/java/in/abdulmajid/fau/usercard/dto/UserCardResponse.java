package in.abdulmajid.fau.usercard.dto;

import in.abdulmajid.fau.card.enums.CardType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCardResponse {

    private Long id;
    private Long cardId;
    private String cardName;
    private String cardSlug;
    private String bankName;
    private String imageUrl;
    private CardType cardType;
    private Boolean isFavorite;
    private String customCardName;
    private String customBankName;
}
