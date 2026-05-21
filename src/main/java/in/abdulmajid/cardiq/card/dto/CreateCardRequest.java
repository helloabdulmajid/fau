package in.abdulmajid.cardiq.card.dto;

import in.abdulmajid.cardiq.card.enums.CardLevel;
import in.abdulmajid.cardiq.card.enums.CardNetwork;
import in.abdulmajid.cardiq.card.enums.CardType;
import in.abdulmajid.cardiq.card.enums.RewardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCardRequest {

    @NotBlank(message = "Card name is required")
    private String name;

    @NotNull(message = "Joining fee is required")
    private Double joiningFee;

    @NotNull(message = "Annual fee is required")
    private Double annualFee;

    @NotNull(message = "Card network is required")
    private CardNetwork network;

    @NotNull(message = "Card type is required")
    private CardType cardType;

    @NotNull(message = "Reward type is required")
    private RewardType rewardType;

    @NotNull(message = "Card level is required")
    private CardLevel cardLevel;

    @NotNull(message = "Bank ID is required")
    private Long bankId;
}