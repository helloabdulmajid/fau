package in.abdulmajid.cardiq.card.entity;


import in.abdulmajid.cardiq.bank.entity.Bank;
import in.abdulmajid.cardiq.card.enums.CardLevel;
import in.abdulmajid.cardiq.card.enums.CardNetwork;
import in.abdulmajid.cardiq.card.enums.CardType;
import in.abdulmajid.cardiq.card.enums.RewardType;
import in.abdulmajid.cardiq.common.BaseEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Card extends BaseEntity {
    private String name;
    private Double joiningFee;
    private Double annualFee;
    private Boolean active = true;
    private Boolean ltf = false;
    @Enumerated(EnumType.STRING)
    private CardNetwork network;
    @Enumerated(EnumType.STRING)
    private CardType cardType;
    @Enumerated(EnumType.STRING)
    private RewardType rewardType;
    private Integer rewardRate;
    private Boolean emiAvailable = false;
    @Enumerated(EnumType.STRING)
    private CardLevel cardLevel;
    private Boolean coBranded = false;
    private String coBrandPartner;
    private String imageUrl;
    private Boolean airportLoungeAccess = false;
    private Boolean railwayLoungeAccess = false;
    private Boolean fuelSurchargeWaiver = false;
    @Column(length = 1000)
    private String description;
    @ManyToOne
    private Bank bank;

   // private Integer monthlySpendWaiver;  later for v2
   // private Double cashbackCap;  later for v2
}