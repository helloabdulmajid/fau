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

    private final MerchantRepository merchantRepository;
    private final OfferRepository offerRepository;

    public MerchantResponse createMerchant(
            CreateMerchantRequest request
    ) {

        if (merchantRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Merchant already exists");
        }

        Merchant merchant = new Merchant();

        merchant.setName(request.getName());
        merchant.setLogoUrl(request.getLogoUrl());
        merchant.setWebsiteUrl(request.getWebsiteUrl());
        merchant.setDescription(request.getDescription());
        merchant.setActive(request.getActive());

        Merchant savedMerchant = merchantRepository.save(merchant);

        return mapToResponse(savedMerchant);
    }

    public List<MerchantResponse> getAllMerchants() {

        return merchantRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MerchantResponse getMerchantById(Long id) {

        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Merchant not found")
                );

        return mapToResponse(merchant);
    }

    public MerchantResponse updateMerchant(
            Long id,
            CreateMerchantRequest request
    ) {

        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Merchant not found")
                );

        merchant.setName(request.getName());
        merchant.setLogoUrl(request.getLogoUrl());
        merchant.setWebsiteUrl(request.getWebsiteUrl());
        merchant.setDescription(request.getDescription());
        merchant.setActive(request.getActive());

        Merchant updatedMerchant = merchantRepository.save(merchant);

        return mapToResponse(updatedMerchant);
    }

    public void deleteMerchant(Long id) {


        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Merchant not found")
                );

        if (offerRepository.existsByMerchant_Id(id)) {
            throw new DuplicateResourceException(
                    "Cannot delete merchant because offers are associated with it"
            );
        }

        merchantRepository.delete(merchant);
    }

    private MerchantResponse mapToResponse(
            Merchant merchant
    ) {

        return MerchantResponse.builder()
                .id(merchant.getId())
                .name(merchant.getName())
                .logoUrl(merchant.getLogoUrl())
                .websiteUrl(merchant.getWebsiteUrl())
                .active(merchant.getActive())
                .description(merchant.getDescription())
                .build();
    }
}