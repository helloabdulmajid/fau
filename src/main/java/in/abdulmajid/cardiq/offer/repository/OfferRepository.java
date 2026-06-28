package in.abdulmajid.cardiq.offer.repository;

import in.abdulmajid.cardiq.benefit.entity.BenefitRule;
import in.abdulmajid.cardiq.offer.entity.Offer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface OfferRepository
        extends JpaRepository<Offer, Long> {

    // =========================================================
    // ACTIVE OFFERS
    // =========================================================

    List<Offer> findByActiveTrue();

    @Query("SELECT o FROM Offer o WHERE o.active = true " +
           "AND (o.startDate IS NULL OR o.startDate <= :today) " +
           "AND (o.endDate IS NULL OR o.endDate >= :today)")
    List<Offer> findActiveValidOffers(LocalDate today);

    // =========================================================
    // RELATION CHECKS
    // =========================================================

    boolean existsByCard_Id(Long cardId);

    boolean existsByMerchant_Id(Long merchantId);

    boolean existsByCategory_Id(Long categoryId);

    boolean existsByBenefitRule(
            BenefitRule benefitRule
    );
}