package in.abdulmajid.fau.merchant.dto;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MerchantResponse {

    // =========================================================
    // BASIC DETAILS
    // =========================================================

    private Long id;

    private String name;

    private String slug;

    private String code;

    private String merchantType;

    private String mccCode;


    // =========================================================
    // VISUAL DETAILS
    // =========================================================

    private String logoUrl;

    private String websiteUrl;

    // =========================================================
    // EXTRA DETAILS
    // =========================================================

    private String description;

    private Boolean active;
}