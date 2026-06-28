package in.abdulmajid.cardiq.usercard.service;

import in.abdulmajid.cardiq.auth.entity.User;
import in.abdulmajid.cardiq.card.entity.Card;
import in.abdulmajid.cardiq.card.repository.CardRepository;
import in.abdulmajid.cardiq.exception.DuplicateResourceException;
import in.abdulmajid.cardiq.exception.ResourceNotFoundException;
import in.abdulmajid.cardiq.usercard.dto.AddCardRequest;
import in.abdulmajid.cardiq.usercard.dto.AddManualCardRequest;
import in.abdulmajid.cardiq.usercard.dto.UserCardResponse;
import in.abdulmajid.cardiq.usercard.entity.UserCard;
import in.abdulmajid.cardiq.usercard.repository.UserCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCardService {

    private final UserCardRepository userCardRepository;
    private final CardRepository cardRepository;

    @Transactional(readOnly = true)
    public List<UserCardResponse> getMyCards(Long userId) {
        return userCardRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public UserCardResponse addCard(User user, AddCardRequest request) {
        if (userCardRepository.existsByUserIdAndCardId(user.getId(), request.getCardId())) {
            throw new DuplicateResourceException("Card already added");
        }

        Card card = cardRepository.findById(request.getCardId())
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        UserCard userCard = new UserCard();
        userCard.setUser(user);
        userCard.setCard(card);
        userCard.setCardType(request.getCardType());

        UserCard saved = userCardRepository.save(userCard);

        return mapToResponse(saved);
    }

    public UserCardResponse addCardManual(User user, AddManualCardRequest request) {
        UserCard userCard = new UserCard();
        userCard.setUser(user);
        userCard.setCustomCardName(request.getCardName());
        userCard.setCustomBankName(request.getBankName());
        userCard.setCardType(request.getCardType());

        UserCard saved = userCardRepository.save(userCard);

        return mapToResponse(saved);
    }

    @Transactional
    public void removeCard(Long userId, Long userCardId) {
        UserCard userCard = userCardRepository.findByIdAndUserId(userCardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found in your collection"));

        userCardRepository.delete(userCard);
    }

    @Transactional
    public UserCardResponse toggleFavorite(Long userId, Long userCardId) {
        UserCard userCard = userCardRepository.findByIdAndUserId(userCardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found in your collection"));

        userCard.setIsFavorite(!Boolean.TRUE.equals(userCard.getIsFavorite()));
        UserCard saved = userCardRepository.save(userCard);

        return mapToResponse(saved);
    }

    private UserCardResponse mapToResponse(UserCard userCard) {
        Card card = userCard.getCard();

        if (card != null) {
            return UserCardResponse.builder()
                    .id(userCard.getId())
                    .cardId(card.getId())
                    .cardName(card.getName())
                    .cardSlug(card.getSlug())
                    .bankName(card.getBank() != null ? card.getBank().getName() : null)
                    .imageUrl(card.getImageUrl())
                    .cardType(userCard.getCardType())
                    .isFavorite(userCard.getIsFavorite())
                    .build();
        }

        return UserCardResponse.builder()
                .id(userCard.getId())
                .cardName(userCard.getCustomCardName())
                .bankName(userCard.getCustomBankName())
                .cardType(userCard.getCardType())
                .isFavorite(userCard.getIsFavorite())
                .build();
    }
}
