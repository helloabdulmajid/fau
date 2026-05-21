package in.abdulmajid.cardiq.merchant.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMerchantRequest {

    @NotBlank(message = "Merchant name is required")
    private String name;

    private String logoUrl;

    private String websiteUrl;
}