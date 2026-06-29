package in.abdulmajid.fau.bank.repository;

import in.abdulmajid.fau.bank.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {

    // =========================================================
    // CHECK DUPLICATE BANK NAME
    // =========================================================

    boolean existsByNameIgnoreCase(String name);

    // =========================================================
    // CHECK DUPLICATE SLUG
    // =========================================================

    boolean existsBySlug(String slug);
}