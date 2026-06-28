package in.abdulmajid.cardiq.usercard.dto;

import in.abdulmajid.cardiq.card.enums.CardType;
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
