package in.abdulmajid.cardiq.merchant.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MerchantResponse {

    private Long id;

    private String name;

    private String logoUrl;

    private String websiteUrl;
    @Column(length = 1000)
    private String description;

    private Boolean active = true;
}