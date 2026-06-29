package in.abdulmajid.fau.category.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequest {

    // =========================================================
    // REQUIRED CATEGORY NAME
    // =========================================================

    @NotBlank(message = "Category name is required")
    private String name;

    // =========================================================
    // OPTIONAL DETAILS
    // =========================================================

    private String slug;

    private String icon;

    private String description;

    private Boolean active = true;
}