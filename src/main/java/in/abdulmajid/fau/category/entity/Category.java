package in.abdulmajid.fau.category.entity;

import in.abdulmajid.fau.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Category extends BaseEntity {

    // =========================================================
    // BASIC CATEGORY DETAILS
    // =========================================================

    // Example:
    // Shopping
    // Travel
    // Fuel
    // Dining

    private String name;

    // Example:
    // shopping
    // travel
    // fuel

    @Column(unique = true)
    private String slug;

    // Example:
    // shopping-cart
    // plane
    // fuel

    private String icon;

    // =========================================================
    // DESCRIPTION
    // =========================================================

    @Column(length = 1000)
    private String description;

    // =========================================================
    // ACTIVE STATUS
    // =========================================================

    private Boolean active = true;
}