package in.abdulmajid.cardiq.card.repository;

import in.abdulmajid.cardiq.card.entity.Card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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
   // Optional<Card> findBySlug(String slug);

    // =========================================================
    // FIND CARD BY SLUG
    // =========================================================
    Optional<Card> findBySlug(String slug);

    // =========================================================
    // SEARCH CARDS BY NAME, BANK NAME, OR CARD TYPE
    // =========================================================

    @Query("SELECT c FROM Card c WHERE c.active = true AND (" +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(c.bank.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(c.cardType) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(c.network) LIKE LOWER(CONCAT('%', :q, '%'))) " +
           "ORDER BY CASE WHEN LOWER(c.name) = LOWER(:q) THEN 0 ELSE 1 END, c.name")
    List<Card> searchByKeyword(@Param("q") String q);

}