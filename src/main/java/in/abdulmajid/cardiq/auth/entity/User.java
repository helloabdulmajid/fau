package in.abdulmajid.cardiq.auth.entity;

import in.abdulmajid.cardiq.auth.enums.UserRole;
import in.abdulmajid.cardiq.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "app_user")
public class User extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    private Boolean active = true;

    private Boolean emailVerified = false;

    private String verificationToken;

    private String phone;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String city;

    private String state;

    private LocalDate dob;

    private String occupation;

    private String annualIncome;

    private String gender;

    private String avatarUrl;

    private String pincode;

    private String preferredCardType;

    private Boolean emailNotifications = true;

    private String resetToken;

    private LocalDateTime resetTokenExpiry;
}
