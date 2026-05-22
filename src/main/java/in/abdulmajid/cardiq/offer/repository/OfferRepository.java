package in.abdulmajid.cardiq.offer.repository;

import in.abdulmajid.cardiq.benefit.entity.BenefitRule;
import in.abdulmajid.cardiq.offer.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

//    List<Offer> findByMerchant_NameContainingIgnoreCaseOrCategory_NameContainingIgnoreCaseOrderByValueDesc(
//            String merchant,
//            String category
//    );

//    List<Offer> findByTitleContainingIgnoreCaseOrMerchant_NameContainingIgnoreCaseOrCategory_NameContainingIgnoreCaseOrCard_NameContainingIgnoreCaseOrCard_Bank_NameContainingIgnoreCaseOrderByPriorityDescValueDesc(
//            String title,
//            String merchant,
//            String category,
//            String card,
//            String bank
//    );

//    List<Offer>   findDistinctByTitleContainingIgnoreCaseOrMerchant_NameContainingIgnoreCaseOrCategory_NameContainingIgnoreCaseOrCard_NameContainingIgnoreCaseOrCard_Bank_NameContainingIgnoreCaseOrderByPriorityDescValueDesc
//            (
//            String title,
//            String merchant,
//            String category,
//            String card,
//            String bank
//    );

    List<Offer>  findDistinctByTitleContainingIgnoreCaseOrMerchant_NameContainingIgnoreCaseOrCategory_NameContainingIgnoreCaseOrCard_NameContainingIgnoreCaseOrCard_Bank_NameContainingIgnoreCase
            (
                    String title,
                    String merchant,
                    String category,
                    String card,
                    String bank
            );



    boolean existsByCard_Id(Long cardId);
    boolean existsByMerchant_Id(Long merchantId);

    boolean existsByCategory_Id(Long categoryId);
    boolean existsByBenefitRule(
            BenefitRule benefitRule
    );

    List<Offer> findByActiveTrue();
}