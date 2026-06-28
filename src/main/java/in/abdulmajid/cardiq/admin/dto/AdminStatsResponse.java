package in.abdulmajid.cardiq.admin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminStatsResponse {

    private long totalBanks;
    private long totalCards;
    private long totalCategories;
    private long totalMerchants;
    private long totalOffers;
    private long totalBenefitRules;
    private long totalUsers;
}
