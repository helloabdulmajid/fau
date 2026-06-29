package in.abdulmajid.fau.merchant.entity;

import in.abdulmajid.fau.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Merchant extends BaseEntity {

    // =========================================================
    // BASIC MERCHANT DETAILS
    // =========================================================

    // Example:
    // Amazon
    // Swiggy
    // Zomato

    private String name;

    // Example:
    // amazon
    // swiggy

    @Column(unique = true)
    private String slug;

    // Example:
    // AMAZON
    // SWIGGY

    private String code;

    // Example:
    // SHOPPING
    // FOOD
    // TRAVEL

    private String merchantType;

    // =========================================================
    // MCC stands for Merchant Category Code.
    // =========================================================


    @Column(name = "mcc_code", length = 4)
    private String mccCode; // Stores the 4-digit card network code (e.g. "Zomato"="5812")

    // =========================================================
    // VISUAL & WEBSITE DETAILS
    // =========================================================

    private String logoUrl;

    private String websiteUrl;

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