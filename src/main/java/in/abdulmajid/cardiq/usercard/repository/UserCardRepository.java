package in.abdulmajid.cardiq.usercard.repository;

import in.abdulmajid.cardiq.usercard.entity.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCardRepository extends JpaRepository<UserCard, Long> {

    List<UserCard> findByUserId(Long userId);

    boolean existsByUserIdAndCardId(Long userId, Long cardId);

    Optional<UserCard> findByIdAndUserId(Long id, Long userId);
}
