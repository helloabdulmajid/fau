package in.abdulmajid.cardiq.bank.service;

import in.abdulmajid.cardiq.bank.dto.BankResponse;
import in.abdulmajid.cardiq.bank.dto.CreateBankRequest;
import in.abdulmajid.cardiq.bank.entity.Bank;
import in.abdulmajid.cardiq.bank.repository.BankRepository;
import in.abdulmajid.cardiq.exception.DuplicateResourceException;
import in.abdulmajid.cardiq.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankService {

    private final BankRepository bankRepository;

    public BankResponse createBank(CreateBankRequest request) {

        if (bankRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Bank already exists");
        }

        Bank bank = new Bank();

        bank.setName(request.getName());
        bank.setLogoUrl(request.getLogoUrl());
        bank.setWebsiteUrl(request.getWebsiteUrl());

        Bank savedBank = bankRepository.save(bank);

        return mapToResponse(savedBank);
    }

    public List<BankResponse> getAllBanks() {

        return bankRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public BankResponse getBankById(Long id) {

        Bank bank = bankRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bank not found")
                );

        return mapToResponse(bank);
    }

    public BankResponse updateBank(
            Long id,
            CreateBankRequest request
    ) {

        Bank bank = bankRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bank not found")
                );

        bank.setName(request.getName());
        bank.setLogoUrl(request.getLogoUrl());
        bank.setWebsiteUrl(request.getWebsiteUrl());

        Bank updatedBank = bankRepository.save(bank);

        return mapToResponse(updatedBank);
    }

    public void deleteBank(Long id) {

        Bank bank = bankRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bank not found")
                );

        bankRepository.delete(bank);
    }

    private BankResponse mapToResponse(Bank bank) {

        return BankResponse.builder()
                .id(bank.getId())
                .name(bank.getName())
                .logoUrl(bank.getLogoUrl())
                .websiteUrl(bank.getWebsiteUrl())
                .build();
    }
}