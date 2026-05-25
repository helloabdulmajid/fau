package in.abdulmajid.cardiq.merchant.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMerchantRequest {

    // =========================================================
    // REQUIRED MERCHANT NAME
    // =========================================================

    @NotBlank(message = "Merchant name is required")
    private String name;

    // =========================================================
    // OPTIONAL DETAILS
    // =========================================================

    private String slug;

    private String code;

    private String merchantType;

    private String mccCode;

    private String logoUrl;

    private String websiteUrl;

    private String description;

    private Boolean active = true;
}