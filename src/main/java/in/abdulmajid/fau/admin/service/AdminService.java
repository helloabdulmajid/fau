package in.abdulmajid.fau.admin.service;

import in.abdulmajid.fau.admin.dto.AdminStatsResponse;
import in.abdulmajid.fau.auth.dto.UserProfileResponse;
import in.abdulmajid.fau.auth.entity.User;
import in.abdulmajid.fau.auth.repository.UserRepository;
import in.abdulmajid.fau.bank.repository.BankRepository;
import in.abdulmajid.fau.benefit.repository.BenefitRuleRepository;
import in.abdulmajid.fau.card.repository.CardRepository;
import in.abdulmajid.fau.category.repository.CategoryRepository;
import in.abdulmajid.fau.merchant.repository.MerchantRepository;
import in.abdulmajid.fau.offer.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final BankRepository bankRepository;
    private final CardRepository cardRepository;
    private final CategoryRepository categoryRepository;
    private final MerchantRepository merchantRepository;
    private final OfferRepository offerRepository;
    private final BenefitRuleRepository benefitRuleRepository;
    private final UserRepository userRepository;

    public AdminStatsResponse getStats() {
        return AdminStatsResponse.builder()
                .totalBanks(bankRepository.count())
                .totalCards(cardRepository.count())
                .totalCategories(categoryRepository.count())
                .totalMerchants(merchantRepository.count())
                .totalOffers(offerRepository.count())
                .totalBenefitRules(benefitRuleRepository.count())
                .totalUsers(userRepository.count())
                .build();
    }

    public List<UserProfileResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    private UserProfileResponse mapToUserResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
