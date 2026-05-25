package in.abdulmajid.cardiq.merchant.service;

import in.abdulmajid.cardiq.exception.DuplicateResourceException;
import in.abdulmajid.cardiq.exception.ResourceNotFoundException;

import in.abdulmajid.cardiq.merchant.dto.CreateMerchantRequest;
import in.abdulmajid.cardiq.merchant.dto.MerchantResponse;
import in.abdulmajid.cardiq.merchant.entity.Merchant;
import in.abdulmajid.cardiq.merchant.repository.MerchantRepository;

import in.abdulmajid.cardiq.offer.repository.OfferRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantService {

    // =========================================================
    // REPOSITORIES
    // =========================================================

    private final MerchantRepository merchantRepository;

    private final OfferRepository offerRepository;

    // =========================================================
    // CREATE MERCHANT
    // =========================================================

    public MerchantResponse createMerchant(
            CreateMerchantRequest request
    ) {

        // -----------------------------------------------------
        // CHECK DUPLICATE NAME
        // -----------------------------------------------------

        if (merchantRepository.existsByNameIgnoreCase(request.getName())) {

            throw new DuplicateResourceException(
                    "Merchant already exists"
            );
        }

        // -----------------------------------------------------
        // CHECK DUPLICATE SLUG
        // -----------------------------------------------------

        if (merchantRepository.existsBySlug(request.getSlug())) {

            throw new DuplicateResourceException(
                    "Merchant slug already exists"
            );
        }

        // -----------------------------------------------------
        // CREATE MERCHANT OBJECT
        // -----------------------------------------------------

        Merchant merchant = new Merchant();

        merchant.setName(request.getName());

        merchant.setSlug(request.getSlug());

        merchant.setCode(request.getCode());

        merchant.setMerchantType(request.getMerchantType());

        merchant.setLogoUrl(request.getLogoUrl());

        merchant.setWebsiteUrl(request.getWebsiteUrl());

        merchant.setDescription(request.getDescription());
        merchant.setMccCode(request.getMccCode());

        merchant.setActive(
                request.getActive() != null
                        ? request.getActive()
                        : true
        );

        // -----------------------------------------------------
        // SAVE MERCHANT
        // -----------------------------------------------------

        Merchant savedMerchant = merchantRepository.save(merchant);

        // -----------------------------------------------------
        // RETURN RESPONSE
        // -----------------------------------------------------

        return mapToResponse(savedMerchant);
    }

    // =========================================================
    // GET ALL MERCHANTS
    // =========================================================

    public List<MerchantResponse> getAllMerchants() {

        return merchantRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // GET MERCHANT BY ID
    // =========================================================

    public MerchantResponse getMerchantById(Long id) {

        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Merchant not found"
                        )
                );

        return mapToResponse(merchant);
    }

    // =========================================================
    // UPDATE MERCHANT
    // =========================================================

    public MerchantResponse updateMerchant(
            Long id,
            CreateMerchantRequest request
    ) {

        // -----------------------------------------------------
        // FIND EXISTING MERCHANT
        // -----------------------------------------------------

        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Merchant not found"
                        )
                );

        // -----------------------------------------------------
        // UPDATE MERCHANT DATA
        // -----------------------------------------------------

        merchant.setName(request.getName());

        merchant.setSlug(request.getSlug());

        merchant.setCode(request.getCode());

        merchant.setMerchantType(request.getMerchantType());

        merchant.setLogoUrl(request.getLogoUrl());

        merchant.setWebsiteUrl(request.getWebsiteUrl());

        merchant.setDescription(request.getDescription());
        merchant.setMccCode(request.getMccCode());

        if (request.getActive() != null) {
            merchant.setActive(request.getActive());
        }

        // -----------------------------------------------------
        // SAVE UPDATED MERCHANT
        // -----------------------------------------------------

        Merchant updatedMerchant = merchantRepository.save(merchant);

        // -----------------------------------------------------
        // RETURN RESPONSE
        // -----------------------------------------------------

        return mapToResponse(updatedMerchant);
    }

    // =========================================================
    // DELETE MERCHANT
    // =========================================================

    public void deleteMerchant(Long id) {

        // -----------------------------------------------------
        // FIND MERCHANT
        // -----------------------------------------------------

        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Merchant not found"
                        )
                );

        // -----------------------------------------------------
        // CHECK ASSOCIATED OFFERS
        // -----------------------------------------------------

        if (offerRepository.existsByMerchant_Id(id)) {

            throw new DuplicateResourceException(
                    "Cannot delete merchant because offers are associated with it"
            );
        }

        // -----------------------------------------------------
        // DELETE MERCHANT
        // -----------------------------------------------------

        merchantRepository.delete(merchant);
    }

    // =========================================================
    // MAP ENTITY TO RESPONSE DTO
    // =========================================================

    private MerchantResponse mapToResponse(
            Merchant merchant
    ) {

        return MerchantResponse.builder()
                .id(merchant.getId())
                .name(merchant.getName())
                .slug(merchant.getSlug())
                .code(merchant.getCode())
                .merchantType(merchant.getMerchantType())
                .logoUrl(merchant.getLogoUrl())
                .websiteUrl(merchant.getWebsiteUrl())
                .description(merchant.getDescription())
                .active(merchant.getActive())
                .mccCode(merchant.getMccCode())
                .build();
    }
}