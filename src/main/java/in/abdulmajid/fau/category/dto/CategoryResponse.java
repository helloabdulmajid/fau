package in.abdulmajid.fau.category.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryResponse {

    // =========================================================
    // BASIC DETAILS
    // =========================================================

    private Long id;

    private String name;

    private String slug;

    private String icon;

    // =========================================================
    // EXTRA DETAILS
    // =========================================================

    private String description;

    private Boolean active;
}