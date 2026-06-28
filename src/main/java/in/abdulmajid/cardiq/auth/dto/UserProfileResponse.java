package in.abdulmajid.cardiq.auth.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String role;
    private Boolean emailVerified;
    private String phone;
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
    private Boolean emailNotifications;
}
