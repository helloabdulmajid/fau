package in.abdulmajid.fau.benefit.repository;

import in.abdulmajid.fau.benefit.entity.BenefitRule;
import in.abdulmajid.fau.common.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BenefitRuleRepository extends JpaRepository<BenefitRule,Long> {
}
