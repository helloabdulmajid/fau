package in.abdulmajid.fau.bank.entity;

import in.abdulmajid.fau.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Bank extends BaseEntity {

    // =========================================================
    // BASIC BANK DETAILS
    // =========================================================

    // Example:
    // State Bank of India

    @Column(unique = true)
    private String name;

    // Example:
    // SBI

    private String shortName;

    // Example:
    // state-bank-of-india

    @Column(unique = true)
    private String slug;

    // Bank logo image URL

    private String logoUrl;

    // Official bank website

    private String websiteUrl;

    // Active/inactive status

    private Boolean active = true;
}