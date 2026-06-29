package in.abdulmajid.fau.benefit.service;

import in.abdulmajid.fau.benefit.dto.BenefitRuleResponse;
import in.abdulmajid.fau.benefit.dto.CreateBenefitRuleRequest;
import in.abdulmajid.fau.benefit.entity.BenefitRule;
import in.abdulmajid.fau.benefit.repository.BenefitRuleRepository;
import in.abdulmajid.fau.exception.DuplicateResourceException;
import in.abdulmajid.fau.exception.ResourceNotFoundException;
import in.abdulmajid.fau.offer.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BenefitRuleService {

    // =========================================================
    // REPOSITORIES
    // =========================================================

    private final BenefitRuleRepository benefitRuleRepository;

    private final OfferRepository offerRepository;

    // =========================================================
    // CREATE RULE
    // =========================================================

    public BenefitRuleResponse createRule(
            CreateBenefitRuleRequest request
    ) {

        BenefitRule rule = new BenefitRule();

        mapRequestToEntity(rule, request);

        BenefitRule savedRule =
                benefitRuleRepository.save(rule);

        return mapToResponse(savedRule);
    }

    // =========================================================
    // GET ALL RULES
    // =========================================================

    public List<BenefitRuleResponse> getAllRules() {

        return benefitRuleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // GET RULE BY ID
    // =========================================================

    public BenefitRuleResponse getRuleById(
            Long id
    ) {

        BenefitRule rule =
                benefitRuleRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Benefit rule not found"
                                )
                        );

        return mapToResponse(rule);
    }

    // =========================================================
    // UPDATE RULE
    // =========================================================

    public BenefitRuleResponse updateRule(
            Long id,
            CreateBenefitRuleRequest request
    ) {

        BenefitRule rule =
                benefitRuleRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Benefit rule not found"
                                )
                        );

        mapRequestToEntity(rule, request);

        BenefitRule updatedRule =
                benefitRuleRepository.save(rule);

        return mapToResponse(updatedRule);
    }

    // =========================================================
    // DELETE RULE
    // =========================================================

    public void deleteRule(Long id) {

        BenefitRule rule =
                benefitRuleRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Benefit rule not found"
                                )
                        );

        // Prevent delete if offers are linked

        if (offerRepository.existsByBenefitRule(rule)) {

            throw new DuplicateResourceException(
                    "Cannot delete benefit rule because offers are associated with it"
            );
        }

        benefitRuleRepository.delete(rule);
    }

    // =========================================================
    // REQUEST DTO -> ENTITY
    // =========================================================

    private void mapRequestToEntity(
            BenefitRule rule,
            CreateBenefitRuleRequest request
    ) {

        rule.setName(
                request.getName()
        );

        rule.setBenefitType(
                request.getBenefitType()
        );

        rule.setRewardPointConversion(
                request.getRewardPointConversion()
        );

        rule.setRedemptionFee(
                request.getRedemptionFee()
        );

        rule.setMinimumRedemptionPoints(
                request.getMinimumRedemptionPoints()
        );

        rule.setExpiryApplicable(
                request.getExpiryApplicable()
        );
        rule.setRedemptionMultiplierPoints(request.getRedemptionMultiplierPoints());

        rule.setMaxRedemptionValuePerMonth(request.getMaxRedemptionValuePerMonth());

        rule.setExpiryMonths(
                request.getExpiryMonths()
        );

        rule.setNotes(
                request.getNotes()
        );
    }

    // =========================================================
    // ENTITY -> RESPONSE DTO
    // =========================================================

    private BenefitRuleResponse mapToResponse(
            BenefitRule rule
    ) {

        return BenefitRuleResponse.builder()
                .id(rule.getId())
                .name(rule.getName())
                .benefitType(rule.getBenefitType())
                .rewardPointConversion(
                        rule.getRewardPointConversion()
                )
                .redemptionFee(
                        rule.getRedemptionFee()
                )
                .minimumRedemptionPoints(
                        rule.getMinimumRedemptionPoints()
                )
                .expiryApplicable(
                        rule.getExpiryApplicable()
                )
                .redemptionMultiplierPoints(rule.getRedemptionMultiplierPoints())

                .maxRedemptionValuePerMonth(rule.getMaxRedemptionValuePerMonth())

                .expiryMonths(
                        rule.getExpiryMonths()
                )
                .notes(
                        rule.getNotes()
                )
                .build();
    }
}