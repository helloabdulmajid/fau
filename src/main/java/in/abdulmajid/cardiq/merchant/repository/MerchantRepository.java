package in.abdulmajid.cardiq.merchant.repository;

import in.abdulmajid.cardiq.merchant.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    boolean existsByNameIgnoreCase(String name);
}