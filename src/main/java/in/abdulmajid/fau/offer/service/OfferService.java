package in.abdulmajid.fau.offer.service;

import in.abdulmajid.fau.benefit.entity.BenefitRule;
import in.abdulmajid.fau.benefit.repository.BenefitRuleRepository;

import in.abdulmajid.fau.card.entity.Card;
import in.abdulmajid.fau.card.repository.CardRepository;

import in.abdulmajid.fau.category.entity.Category;
import in.abdulmajid.fau.category.repository.CategoryRepository;

import in.abdulmajid.fau.exception.DuplicateResourceException;
import in.abdulmajid.fau.exception.ResourceNotFoundException;

import in.abdulmajid.fau.merchant.entity.Merchant;
import in.abdulmajid.fau.merchant.repository.MerchantRepository;

import in.abdulmajid.fau.offer.dto.CreateOfferRequest;
import in.abdulmajid.fau.offer.dto.OfferResponse;

import in.abdulmajid.fau.offer.entity.Offer;
import in.abdulmajid.fau.offer.repository.OfferRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {

    // =========================================================
    // REPOSITORIES
    // =========================================================

    private final OfferRepository offerRepository;

    private final CardRepository cardRepository;

    private final MerchantRepository merchantRepository;

    private final CategoryRepository categoryRepository;

    private final BenefitRuleRepository benefitRuleRepository;

    // =========================================================
    // CREATE OFFER
    // =========================================================

    public OfferResponse createOffer(
            CreateOfferRequest request
    ) {

        // -----------------------------------------------------
        // VALIDATE DATES
        // -----------------------------------------------------

        validateDates(request);

        // -----------------------------------------------------
        // FETCH RELATIONS
        // -----------------------------------------------------

        Card card = getCard(request.getCardId());

        Merchant merchant = getMerchant(
                request.getMerchantId()
        );

        Category category = getCategory(
                request.getCategoryId()
        );

        BenefitRule benefitRule =
                getBenefitRule(
                        request.getBenefitRuleId()
                );

        // -----------------------------------------------------
        // CREATE OFFER ENTITY
        // -----------------------------------------------------

        Offer offer = new Offer();

        mapRequestToEntity(
                offer,
                request,
                card,
                merchant,
                category,
                benefitRule
        );

        // -----------------------------------------------------
        // SAVE OFFER
        // -----------------------------------------------------

        Offer savedOffer = offerRepository.save(offer);

        // -----------------------------------------------------
        // RETURN RESPONSE
        // -----------------------------------------------------

        return mapToResponse(savedOffer);
    }

    // =========================================================
    // GET ALL OFFERS
    // =========================================================

    public List<OfferResponse> getAllOffers() {

        return offerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // GET OFFER BY ID
    // =========================================================

    public OfferResponse getOfferById(Long id) {

        Offer offer = offerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Offer not found"
                        )
                );

        return mapToResponse(offer);
    }

    // =========================================================
    // UPDATE OFFER
    // =========================================================

    public OfferResponse updateOffer(
            Long id,
            CreateOfferRequest request
    ) {

        // -----------------------------------------------------
        // VALIDATE DATES
        // -----------------------------------------------------

        validateDates(request);

        // -----------------------------------------------------
        // FIND EXISTING OFFER
        // -----------------------------------------------------

        Offer offer = offerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Offer not found"
                        )
                );

        // -----------------------------------------------------
        // FETCH RELATIONS
        // -----------------------------------------------------

        Card card = getCard(request.getCardId());

        Merchant merchant = getMerchant(
                request.getMerchantId()
        );

        Category category = getCategory(
                request.getCategoryId()
        );

        BenefitRule benefitRule =
                getBenefitRule(
                        request.getBenefitRuleId()
                );

        // -----------------------------------------------------
        // UPDATE OFFER ENTITY
        // -----------------------------------------------------

        mapRequestToEntity(
                offer,
                request,
                card,
                merchant,
                category,
                benefitRule
        );

        // -----------------------------------------------------
        // SAVE UPDATED OFFER
        // -----------------------------------------------------

        Offer updatedOffer = offerRepository.save(offer);

        // -----------------------------------------------------
        // RETURN RESPONSE
        // -----------------------------------------------------

        return mapToResponse(updatedOffer);
    }

    // =========================================================
    // DELETE OFFER
    // =========================================================

    public void deleteOffer(Long id) {

        // -----------------------------------------------------
        // FIND OFFER
        // -----------------------------------------------------

        Offer offer = offerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Offer not found"
                        )
                );

        // -----------------------------------------------------
        // DELETE OFFER
        // -----------------------------------------------------

        offerRepository.delete(offer);
    }

    // =========================================================
    // VALIDATE DATES
    // =========================================================

    private void validateDates(
            CreateOfferRequest request
    ) {

        if (
                request.getEndDate()
                        .isBefore(request.getStartDate())
        ) {

            throw new DuplicateResourceException(
                    "End date cannot be before start date"
            );
        }
    }

    // =========================================================
    // FIND CARD
    // =========================================================

    private Card getCard(Long id) {

        return cardRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Card not found"
                        )
                );
    }

    // =========================================================
    // FIND MERCHANT
    // =========================================================

    private Merchant getMerchant(Long id) {

        return merchantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Merchant not found"
                        )
                );
    }

    // =========================================================
    // FIND CATEGORY
    // =========================================================

    private Category getCategory(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"
                        )
                );
    }

    // =========================================================
    // FIND BENEFIT RULE
    // =========================================================

    private BenefitRule getBenefitRule(Long id) {

        return benefitRuleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Benefit rule not found"
                        )
                );
    }

    // =========================================================
    // MAP REQUEST DTO TO ENTITY
    // =========================================================

    private void mapRequestToEntity(
            Offer offer,
            CreateOfferRequest request,
            Card card,
            Merchant merchant,
            Category category,
            BenefitRule benefitRule
    ) {

        // -----------------------------------------------------
        // BASIC OFFER DETAILS
        // -----------------------------------------------------

        offer.setTitle(request.getTitle());

        offer.setDescription(
                request.getDescription()
        );

        offer.setOfferType(
                request.getOfferType()
        );

        offer.setValue(request.getValue());

        offer.setActive(
                request.getActive()
        );

        // -----------------------------------------------------
        // BENEFIT DETAILS
        // -----------------------------------------------------

        offer.setMaxBenefit(
                request.getMaxBenefit()
        );

        offer.setMinimumSpend(
                request.getMinimumSpend()
        );

        offer.setCashbackCap(
                request.getCashbackCap()
        );

        // -----------------------------------------------------
        // DATE DETAILS
        // -----------------------------------------------------

        offer.setStartDate(
                request.getStartDate()
        );

        offer.setEndDate(
                request.getEndDate()
        );

        offer.setVerifiedAt(
                request.getVerifiedAt()
        );

        // -----------------------------------------------------
        // SOURCE DETAILS
        // -----------------------------------------------------

        offer.setSourceUrl(
                request.getSourceUrl()
        );

        // -----------------------------------------------------
        // PLATFORM DETAILS
        // -----------------------------------------------------

        offer.setPlatform(
                request.getPlatform()
        );

        offer.setBenefitPeriod(
                request.getBenefitPeriod()
        );

        offer.setApplicableNetwork(
                request.getApplicableNetwork()
        );

        // -----------------------------------------------------
        // SPECIAL CONDITIONS
        // -----------------------------------------------------

        offer.setWeekendOnly(
                request.getWeekendOnly()
        );

        offer.setOnlineOnly(
                request.getOnlineOnly()
        );

        offer.setRequiresMembership(
                request.getRequiresMembership()
        );

        offer.setExcludedMerchants(
                request.getExcludedMerchants()
        );

        // -----------------------------------------------------
        // EXTRA OFFER DETAILS
        // -----------------------------------------------------

        offer.setMilestoneBenefit(
                request.getMilestoneBenefit()
        );

        offer.setLimitedTimeOffer(
                request.getLimitedTimeOffer()
        );

        offer.setPriority(
                request.getPriority()
        );

        offer.setPermanentOffer(
                request.getPermanentOffer()
        );

        // -----------------------------------------------------
        // RELATIONS
        // -----------------------------------------------------

        offer.setCard(card);

        offer.setMerchant(merchant);

        offer.setCategory(category);

        offer.setBenefitRule(benefitRule);
    }

    // =========================================================
    // MAP ENTITY TO RESPONSE DTO
    // =========================================================

    private OfferResponse mapToResponse(
            Offer offer
    ) {

        return OfferResponse.builder()

                // -------------------------------------------------
                // BASIC OFFER DETAILS
                // -------------------------------------------------

                .id(offer.getId())

                .title(offer.getTitle())

                .description(
                        offer.getDescription()
                )

                .offerType(
                        offer.getOfferType()
                )

                .value(
                        offer.getValue()
                )

                .active(
                        offer.getActive()
                )

                // -------------------------------------------------
                // BENEFIT DETAILS
                // -------------------------------------------------

                .maxBenefit(
                        offer.getMaxBenefit()
                )

                .minimumSpend(
                        offer.getMinimumSpend()
                )

                .cashbackCap(
                        offer.getCashbackCap()
                )

                // -------------------------------------------------
                // DATE DETAILS
                // -------------------------------------------------

                .startDate(
                        offer.getStartDate()
                )

                .endDate(
                        offer.getEndDate()
                )

                .verifiedAt(
                        offer.getVerifiedAt()
                )

                // -------------------------------------------------
                // SOURCE DETAILS
                // -------------------------------------------------

                .sourceUrl(
                        offer.getSourceUrl()
                )

                // -------------------------------------------------
                // RELATION DETAILS
                // -------------------------------------------------

                .cardName(
                        offer.getCard().getName()
                )

                .merchantName(
                        offer.getMerchant().getName()
                )

                .categoryName(
                        offer.getCategory().getName()
                )

                // -------------------------------------------------
                // PLATFORM DETAILS
                // -------------------------------------------------

                .platform(
                        offer.getPlatform()
                )

                .benefitPeriod(
                        offer.getBenefitPeriod()
                )

                .applicableNetwork(
                        offer.getApplicableNetwork()
                )

                // -------------------------------------------------
                // SPECIAL CONDITIONS
                // -------------------------------------------------

                .weekendOnly(
                        offer.getWeekendOnly()
                )

                .onlineOnly(
                        offer.getOnlineOnly()
                )

                .requiresMembership(
                        offer.getRequiresMembership()
                )

                .excludedMerchants(
                        offer.getExcludedMerchants()
                )

                // -------------------------------------------------
                // EXTRA OFFER DETAILS
                // -------------------------------------------------

                .milestoneBenefit(
                        offer.getMilestoneBenefit()
                )

                .limitedTimeOffer(
                        offer.getLimitedTimeOffer()
                )

                .priority(
                        offer.getPriority()
                )

                .permanentOffer(
                        offer.getPermanentOffer()
                )

                // -------------------------------------------------
                // BENEFIT RULE DETAILS
                // -------------------------------------------------

                .benefitRuleName(
                        offer.getBenefitRule().getName()
                )

                .benefitType(
                        offer.getBenefitRule()
                                .getBenefitType()
                )

                .build();
    }
}