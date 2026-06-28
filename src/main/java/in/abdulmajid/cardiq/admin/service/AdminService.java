package in.abdulmajid.cardiq.admin.service;

import in.abdulmajid.cardiq.admin.dto.AdminStatsResponse;
import in.abdulmajid.cardiq.auth.dto.UserProfileResponse;
import in.abdulmajid.cardiq.auth.entity.User;
import in.abdulmajid.cardiq.auth.repository.UserRepository;
import in.abdulmajid.cardiq.bank.repository.BankRepository;
import in.abdulmajid.cardiq.benefit.repository.BenefitRuleRepository;
import in.abdulmajid.cardiq.card.repository.CardRepository;
import in.abdulmajid.cardiq.category.repository.CategoryRepository;
import in.abdulmajid.cardiq.merchant.repository.MerchantRepository;
import in.abdulmajid.cardiq.offer.repository.OfferRepository;
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
