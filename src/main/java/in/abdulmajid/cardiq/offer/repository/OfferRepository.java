package in.abdulmajid.cardiq.offer.repository;

import in.abdulmajid.cardiq.offer.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByMerchant_NameContainingIgnoreCaseOrCategory_NameContainingIgnoreCaseOrderByValueDesc(
            String merchant,
            String category
    );
}