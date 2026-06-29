package in.abdulmajid.fau.usercard.service;

import in.abdulmajid.fau.auth.entity.User;
import in.abdulmajid.fau.card.entity.Card;
import in.abdulmajid.fau.card.repository.CardRepository;
import in.abdulmajid.fau.exception.DuplicateResourceException;
import in.abdulmajid.fau.exception.ResourceNotFoundException;
import in.abdulmajid.fau.usercard.dto.AddCardRequest;
import in.abdulmajid.fau.usercard.dto.AddManualCardRequest;
import in.abdulmajid.fau.usercard.dto.UserCardResponse;
import in.abdulmajid.fau.usercard.entity.UserCard;
import in.abdulmajid.fau.usercard.repository.UserCardRepository;
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

    @Transactional
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

    @Transactional
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
