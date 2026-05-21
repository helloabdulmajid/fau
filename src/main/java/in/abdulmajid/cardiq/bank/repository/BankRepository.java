package in.abdulmajid.cardiq.bank.repository;

import in.abdulmajid.cardiq.bank.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {

    boolean existsByNameIgnoreCase(String name);
}