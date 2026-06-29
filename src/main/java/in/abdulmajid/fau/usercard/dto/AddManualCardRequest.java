package in.abdulmajid.fau.usercard.dto;

import in.abdulmajid.fau.card.enums.CardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddManualCardRequest {

    @NotBlank(message = "Card name is required")
    private String cardName;

    @NotBlank(message = "Bank name is required")
    private String bankName;

    @NotNull(message = "Card type is required")
    private CardType cardType;
}
