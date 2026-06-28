package in.abdulmajid.cardiq.card.service;

import in.abdulmajid.cardiq.bank.entity.Bank;
import in.abdulmajid.cardiq.bank.repository.BankRepository;

import in.abdulmajid.cardiq.card.dto.CardFilterRequest;
import in.abdulmajid.cardiq.card.dto.CardResponse;
import in.abdulmajid.cardiq.card.dto.CreateCardRequest;
import in.abdulmajid.cardiq.card.dto.SimpleCardResponse;

import in.abdulmajid.cardiq.card.entity.Card;

import in.abdulmajid.cardiq.card.repository.CardRepository;

import in.abdulmajid.cardiq.card.specification.CardSpecification;

import in.abdulmajid.cardiq.cloudinary.CloudinaryService;
import in.abdulmajid.cardiq.exception.DuplicateResourceException;
import in.abdulmajid.cardiq.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    // =========================================================
    // REPOSITORIES
    // =========================================================

    private final CardRepository cardRepository;

    private final BankRepository bankRepository;

    private final CloudinaryService cloudinaryService;

    // =========================================================
    // CREATE CARD
    // =========================================================

    public CardResponse createCard(
            CreateCardRequest request
    ) {

        // -----------------------------------------------------
        // CHECK DUPLICATE CARD NAME
        // -----------------------------------------------------

        if (cardRepository.existsByNameIgnoreCase(request.getName())) {

            throw new DuplicateResourceException(
                    "Card already exists"
            );
        }

        // -----------------------------------------------------
        // CHECK DUPLICATE CARD SLUG
        // -----------------------------------------------------

        if (cardRepository.existsBySlug(request.getSlug())) {

            throw new DuplicateResourceException(
                    "Card slug already exists"
            );
        }

        // -----------------------------------------------------
        // FIND BANK
        // -----------------------------------------------------

        Bank bank = bankRepository.findById(request.getBankId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bank not found"
                        )
                );

        // -----------------------------------------------------
        // CREATE CARD OBJECT
        // -----------------------------------------------------

        Card card = new Card();

        mapRequestToEntity(card, request, bank);

        // -----------------------------------------------------
        // SAVE CARD
        // -----------------------------------------------------

        Card savedCard = cardRepository.save(card);

        // -----------------------------------------------------
        // RETURN RESPONSE
        // -----------------------------------------------------

        return mapToResponse(savedCard);
    }

    // =========================================================
    // GET ALL CARDS
    // =========================================================

    public List<CardResponse> getAllCards() {

        return cardRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // GET CARD BY ID
    // =========================================================

    public CardResponse getCardById(Long id) {

        Card card = cardRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
           "Card not found"
                        )
                );
        return mapToResponse(card);

    }

    // =========================================================
    // GET CARD BY SLUG
    // =========================================================

    public CardResponse getCardBySlug(String slug) {

        Card card = cardRepository.findBySlug(slug)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Card not found"
                        )
                );
        return mapToResponse(card);
    }

    // =========================================================
    // FILTER CARDS
    // =========================================================

    public List<CardResponse> filterCards(
            CardFilterRequest filter
    ) {

        return cardRepository.findAll(
                        CardSpecification.filterCards(filter)
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // UPDATE CARD
    // =========================================================

    public CardResponse updateCard(
            Long id,
            CreateCardRequest request
    ) {

        // -----------------------------------------------------
        // FIND EXISTING CARD
        // -----------------------------------------------------

        Card card = cardRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Card not found"
                        )
                );

        // -----------------------------------------------------
        // FIND BANK
        // -----------------------------------------------------

        Bank bank = bankRepository.findById(request.getBankId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bank not found"
                        )
                );

        // -----------------------------------------------------
        // DELETE OLD IMAGE IF REPLACED
        // -----------------------------------------------------

        String oldImageUrl = card.getImageUrl();

        // -----------------------------------------------------
        // UPDATE CARD DATA
        // -----------------------------------------------------

        mapRequestToEntity(card, request, bank);

        // -----------------------------------------------------
        // SAVE UPDATED CARD
        // -----------------------------------------------------

        Card updatedCard = cardRepository.save(card);

        // -----------------------------------------------------
        // DELETE OLD IMAGE FROM CLOUDINARY
        // -----------------------------------------------------

        if (oldImageUrl != null && !oldImageUrl.equals(card.getImageUrl())) {
            cloudinaryService.deleteImage(oldImageUrl);
        }

        // -----------------------------------------------------
        // RETURN RESPONSE
        // -----------------------------------------------------

        return mapToResponse(updatedCard);
    }

    // =========================================================
    // DELETE CARD
    // =========================================================

    public void deleteCard(Long id) {

        // -----------------------------------------------------
        // FIND CARD
        // -----------------------------------------------------

        Card card = cardRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Card not found"
                        )
                );

        // -----------------------------------------------------
        // DELETE CARD IMAGE FROM CLOUDINARY
        // -----------------------------------------------------

        cloudinaryService.deleteImage(card.getImageUrl());

        // -----------------------------------------------------
        // DELETE CARD
        // -----------------------------------------------------

        cardRepository.delete(card);
    }

    // =========================================================
    // MAP REQUEST DTO TO ENTITY
    // =========================================================

    private void mapRequestToEntity(
            Card card,
            CreateCardRequest request,
            Bank bank
    ) {

        // -----------------------------------------------------
        // BASIC CARD DETAILS
        // -----------------------------------------------------

        card.setName(request.getName());

        card.setSlug(request.getSlug());

        card.setDescription(request.getDescription());

        card.setImageUrl(request.getImageUrl());

        card.setActive(
                request.getActive() != null
                        ? request.getActive()
                        : true
        );

        card.setLtf(
                request.getLtf() != null
                        ? request.getLtf()
                        : false
        );

        // -----------------------------------------------------
        // CARD FEES
        // -----------------------------------------------------

        card.setJoiningFee(request.getJoiningFee());

        card.setAnnualFee(request.getAnnualFee());

        card.setForexMarkup(request.getForexMarkup());

        // -----------------------------------------------------
        // CARD CLASSIFICATION
        // -----------------------------------------------------

        card.setNetwork(request.getNetwork());

        card.setCardType(request.getCardType());

        card.setCardLevel(request.getCardLevel());

        card.setRewardType(request.getRewardType());

        card.setRewardValueType(
                request.getRewardValueType()
        );

        // -----------------------------------------------------
        // REWARD DETAILS
        // -----------------------------------------------------

        card.setBaseRewardValue(
                request.getBaseRewardValue()
        );

        card.setRewardPointConversion(
                request.getRewardPointConversion()
        );

        card.setRewardPointExpiryMonths(
                request.getRewardPointExpiryMonths()
        );

        // -----------------------------------------------------
        // CARD FEATURES
        // -----------------------------------------------------

        card.setEmiAvailable(
                request.getEmiAvailable()
        );

        card.setFuelSurchargeWaiver(
                request.getFuelSurchargeWaiver()
        );

        card.setAirportLoungeAccess(
                request.getAirportLoungeAccess()
        );

        card.setRailwayLoungeAccess(
                request.getRailwayLoungeAccess()
        );

        card.setAddOnCardAvailable(
                request.getAddOnCardAvailable()
        );

        card.setContactlessEnabled(
                request.getContactlessEnabled()
        );

        // -----------------------------------------------------
        // LOUNGE ACCESS DETAILS
        // -----------------------------------------------------

        card.setDomesticLoungeAccess(
                request.getDomesticLoungeAccess()
        );

        card.setInternationalLoungeAccess(
                request.getInternationalLoungeAccess()
        );

        card.setDomesticLoungePeriod(
                request.getDomesticLoungePeriod()
        );

        card.setInternationalLoungePeriod(
                request.getInternationalLoungePeriod()
        );

        // -----------------------------------------------------
        // CO-BRANDED DETAILS
        // -----------------------------------------------------

        card.setCoBranded(
                request.getCoBranded()
        );

        card.setCoBrandPartner(
                request.getCoBrandPartner()
        );

        // -----------------------------------------------------
        // ELIGIBILITY
        // -----------------------------------------------------

        card.setMinimumIncomeRequired(
                request.getMinimumIncomeRequired()
        );

        // =========================================================
        // Digital & Network Flags
        // =========================================================

        card.setUpiEnabled(request.getUpiEnabled());
        card.setIsVirtualOnly(request.getIsVirtualOnly());
        card.setRequiresPrimaryCard(request.getRequiresPrimaryCard());

        // -----------------------------------------------------
        // RELATIONS
        // -----------------------------------------------------

        card.setBank(bank);
    }

    // =========================================================
    // MAP ENTITY TO RESPONSE DTO
    // =========================================================

    private CardResponse mapToResponse(
            Card card
    ) {

        return CardResponse.builder()

                // -------------------------------------------------
                // BASIC CARD DETAILS
                // -------------------------------------------------

                .id(card.getId())

                .name(card.getName())

                .slug(card.getSlug())

                .description(card.getDescription())

                .imageUrl(card.getImageUrl())

                .active(card.getActive())

                .ltf(card.getLtf())

                // -------------------------------------------------
                // BANK DETAILS
                // -------------------------------------------------

                .bankName(
                        card.getBank() != null
                                ? card.getBank().getName()
                                : null
                )
                .bankId(
                        card.getBank() != null
                                ? card.getBank().getId()
                                : null
                )

                // -------------------------------------------------
                // CARD CLASSIFICATION
                // -------------------------------------------------

                .network(card.getNetwork())

                .cardType(card.getCardType())

                .rewardType(card.getRewardType())

                .cardLevel(card.getCardLevel())

                .rewardValueType(
                        card.getRewardValueType()
                )

                // -------------------------------------------------
                // CARD FEES
                // -------------------------------------------------

                .joiningFee(card.getJoiningFee())

                .annualFee(card.getAnnualFee())

                .forexMarkup(card.getForexMarkup())

                // -------------------------------------------------
                // REWARD DETAILS
                // -------------------------------------------------

                .baseRewardValue(
                        card.getBaseRewardValue()
                )

                .rewardPointConversion(
                        card.getRewardPointConversion()
                )

                .rewardPointExpiryMonths(
                        card.getRewardPointExpiryMonths()
                )

                // -------------------------------------------------
                // CARD FEATURES
                // -------------------------------------------------

                .emiAvailable(card.getEmiAvailable())

                .fuelSurchargeWaiver(
                        card.getFuelSurchargeWaiver()
                )

                .airportLoungeAccess(
                        card.getAirportLoungeAccess()
                )

                .railwayLoungeAccess(
                        card.getRailwayLoungeAccess()
                )

                .addOnCardAvailable(
                        card.getAddOnCardAvailable()
                )

                .contactlessEnabled(
                        card.getContactlessEnabled()
                )

                // -------------------------------------------------
                // LOUNGE ACCESS DETAILS
                // -------------------------------------------------

                .domesticLoungeAccess(
                        card.getDomesticLoungeAccess()
                )

                .internationalLoungeAccess(
                        card.getInternationalLoungeAccess()
                )

                .domesticLoungePeriod(
                        card.getDomesticLoungePeriod()
                )

                .internationalLoungePeriod(
                        card.getInternationalLoungePeriod()
                )

                // -------------------------------------------------
                // CO-BRANDED DETAILS
                // -------------------------------------------------

                .coBranded(card.getCoBranded())

                .coBrandPartner(
                        card.getCoBrandPartner()
                )

                  // =========================================================
                  // Digital & Network Flags
                  // =========================================================

                .upiEnabled(card.getUpiEnabled())
                .isVirtualOnly(card.getIsVirtualOnly())
                .requiresPrimaryCard(card.getRequiresPrimaryCard())


                 // -------------------------------------------------
                // ELIGIBILITY
                // -------------------------------------------------

                .minimumIncomeRequired(
                        card.getMinimumIncomeRequired()
                )

                .build();
    }

    // =========================================================
    // SEARCH CARDS BY KEYWORD (NAME / BANK / TYPE)
    // =========================================================

    public List<SimpleCardResponse> searchCardsByKeyword(String keyword) {
        String normalized = keyword.trim().toLowerCase()
                .replace("american express", "amex")
                .replace("master card", "mastercard");
        return cardRepository.searchByKeyword(normalized).stream()
                .map(this::mapToSimpleResponse)
                .toList();
    }

    // =========================================================
    // MAP ENTITY TO SIMPLE RESPONSE DTO
    // =========================================================

    private SimpleCardResponse mapToSimpleResponse(Card card) {
        return SimpleCardResponse.builder()
                .id(card.getId())
                .cardName(card.getName())
                .bankName(
                        card.getBank() != null
                                ? card.getBank().getName()
                                : null
                )
                .imageUrl(card.getImageUrl())
                .cardType(card.getCardType())
                .network(card.getNetwork())
                .build();
    }


}