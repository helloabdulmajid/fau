package in.abdulmajid.cardiq.merchant.entity;

import in.abdulmajid.cardiq.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Merchant extends BaseEntity {

    private String name;

    private String logoUrl;

    private String websiteUrl;
    @Column(length = 1000)
    private String description;

    private Boolean active = true;
}