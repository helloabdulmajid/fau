package in.abdulmajid.fau.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

    private String token;
    private Long id;
    private String name;
    private String email;
    private String role;
    private Boolean emailVerified;
}
