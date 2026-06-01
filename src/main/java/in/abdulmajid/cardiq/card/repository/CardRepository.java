package in.abdulmajid.cardiq.card.repository;

import in.abdulmajid.cardiq.card.entity.Card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CardRepository
        extends JpaRepository<Card, Long>,
        JpaSpecificationExecutor<Card> {

    // =========================================================
    // CHECK ASSOCIATED BANK
    // =========================================================

    boolean existsByBank_Id(Long bankId);

    // =========================================================
    // CHECK DUPLICATE CARD NAME
    // =========================================================

    boolean existsByNameIgnoreCase(String name);

    // =========================================================
    // CHECK DUPLICATE CARD SLUG
    // =========================================================

    boolean existsBySlug(String slug);
    Optional<Card> findBySlug(String slug);
}