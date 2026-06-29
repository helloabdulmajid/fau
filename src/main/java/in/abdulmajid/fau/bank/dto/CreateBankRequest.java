package in.abdulmajid.fau.bank.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBankRequest {

    // =========================================================
    // REQUIRED BANK NAME
    // =========================================================

    @NotBlank(message = "Bank name is required")
    private String name;

    // =========================================================
    // OPTIONAL DETAILS
    // =========================================================

    private String shortName;

    private String slug;

    private String logoUrl;

    private String websiteUrl;

    private Boolean active;
}