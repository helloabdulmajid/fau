package in.abdulmajid.cardiq.card.dto;

import in.abdulmajid.cardiq.card.enums.CardLevel;
import in.abdulmajid.cardiq.card.enums.CardNetwork;
import in.abdulmajid.cardiq.card.enums.CardType;
import in.abdulmajid.cardiq.card.enums.RewardType;
import jakarta.validation.constraints.Min;
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
    @Min(value = 0, message = "Joining fee cannot be negative")
    private Double joiningFee;

    @NotNull(message = "Annual fee is required")
    @Min(value = 0, message = "Annual fee cannot be negative")
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

    private Boolean ltf = false;

    private Boolean airportLoungeAccess = false;

    private Boolean railwayLoungeAccess = false;

    private Boolean fuelSurchargeWaiver = false;

    private Boolean coBranded = false;

    private Boolean emiAvailable = false;

    private Integer rewardRate;

    private String description;
    private Boolean active = true;

    private String coBrandPartner;

    private String imageUrl;
}