package in.abdulmajid.fau.usercard.entity;

import in.abdulmajid.fau.auth.entity.User;
import in.abdulmajid.fau.card.entity.Card;
import in.abdulmajid.fau.card.enums.CardType;
import in.abdulmajid.fau.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "card_id"}))
public class UserCard extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private Card card;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    private Boolean isFavorite = false;

    private String customCardName;

    private String customBankName;
}
