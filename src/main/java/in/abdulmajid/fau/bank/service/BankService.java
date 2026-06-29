package in.abdulmajid.fau.bank.service;

import in.abdulmajid.fau.bank.dto.BankResponse;
import in.abdulmajid.fau.bank.dto.CreateBankRequest;
import in.abdulmajid.fau.bank.entity.Bank;
import in.abdulmajid.fau.bank.repository.BankRepository;
import in.abdulmajid.fau.card.repository.CardRepository;
import in.abdulmajid.fau.exception.DuplicateResourceException;
import in.abdulmajid.fau.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankService {

    // =========================================================
    // REPOSITORIES
    // =========================================================

    private final BankRepository bankRepository;

    private final CardRepository cardRepository;

    // =========================================================
    // CREATE BANK
    // =========================================================

    public BankResponse createBank(CreateBankRequest request) {

        // -----------------------------------------------------
        // CHECK DUPLICATE BANK NAME
        // -----------------------------------------------------

        if (bankRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException(
                    "Bank already exists"
            );
        }

        // -----------------------------------------------------
        // CHECK DUPLICATE SLUG
        // -----------------------------------------------------

        if (bankRepository.existsBySlug(request.getSlug())) {
            throw new DuplicateResourceException(
                    "Bank slug already exists"
            );
        }

        // -----------------------------------------------------
        // CREATE NEW BANK OBJECT
        // -----------------------------------------------------

        Bank bank = new Bank();

        bank.setName(request.getName());

        bank.setShortName(request.getShortName());

        bank.setSlug(request.getSlug());

        bank.setLogoUrl(request.getLogoUrl());

        bank.setWebsiteUrl(request.getWebsiteUrl());

        bank.setActive(
                request.getActive() != null
                        ? request.getActive()
                        : true
        );

        // -----------------------------------------------------
        // SAVE BANK
        // -----------------------------------------------------

        Bank savedBank = bankRepository.save(bank);

        // -----------------------------------------------------
        // RETURN RESPONSE
        // -----------------------------------------------------

        return mapToResponse(savedBank);
    }

    // =========================================================
    // GET ALL BANKS
    // =========================================================

    public List<BankResponse> getAllBanks() {

        return bankRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // =========================================================
    // GET BANK BY ID
    // =========================================================

    public BankResponse getBankById(Long id) {

        Bank bank = bankRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bank not found"
                        )
                );

        return mapToResponse(bank);
    }

    // =========================================================
    // UPDATE BANK
    // =========================================================

    public BankResponse updateBank(
            Long id,
            CreateBankRequest request
    ) {

        // -----------------------------------------------------
        // FIND EXISTING BANK
        // -----------------------------------------------------

        Bank bank = bankRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bank not found"
                        )
                );

        // -----------------------------------------------------
        // UPDATE BANK DATA
        // -----------------------------------------------------

        bank.setName(request.getName());

        bank.setShortName(request.getShortName());

        bank.setSlug(request.getSlug());

        bank.setLogoUrl(request.getLogoUrl());

        bank.setWebsiteUrl(request.getWebsiteUrl());

        if (request.getActive() != null) {
            bank.setActive(request.getActive());
        }

        // -----------------------------------------------------
        // SAVE UPDATED BANK
        // -----------------------------------------------------

        Bank updatedBank = bankRepository.save(bank);

        // -----------------------------------------------------
        // RETURN RESPONSE
        // -----------------------------------------------------

        return mapToResponse(updatedBank);
    }

    // =========================================================
    // DELETE BANK
    // =========================================================

    public void deleteBank(Long id) {

        // -----------------------------------------------------
        // FIND BANK
        // -----------------------------------------------------

        Bank bank = bankRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bank not found"
                        )
                );

        // -----------------------------------------------------
        // CHECK ASSOCIATED CARDS
        // -----------------------------------------------------

        if (cardRepository.existsByBank_Id(id)) {

            throw new DuplicateResourceException(
                    "Cannot delete bank because cards are associated with it"
            );
        }

        // -----------------------------------------------------
        // DELETE BANK
        // -----------------------------------------------------

        bankRepository.delete(bank);
    }

    // =========================================================
    // MAP ENTITY TO RESPONSE DTO
    // =========================================================

    private BankResponse mapToResponse(Bank bank) {

        return BankResponse.builder()
                .id(bank.getId())
                .name(bank.getName())
                .shortName(bank.getShortName())
                .slug(bank.getSlug())
                .logoUrl(bank.getLogoUrl())
                .websiteUrl(bank.getWebsiteUrl())
                .active(bank.getActive())
                .build();
    }
}