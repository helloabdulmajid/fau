package in.abdulmajid.fau.auth.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateProfileRequest {

    private String name;
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
