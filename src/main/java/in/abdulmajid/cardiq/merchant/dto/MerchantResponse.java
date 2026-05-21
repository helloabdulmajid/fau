package in.abdulmajid.cardiq.merchant.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MerchantResponse {

    private Long id;

    private String name;

    private String logoUrl;

    private String websiteUrl;
}