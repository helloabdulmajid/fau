package in.abdulmajid.fau.usercard.dto;

import in.abdulmajid.fau.card.enums.CardType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCardRequest {

    @NotNull(message = "Card ID is required")
    private Long cardId;

    @NotNull(message = "Card type is required")
    private CardType cardType;
}
