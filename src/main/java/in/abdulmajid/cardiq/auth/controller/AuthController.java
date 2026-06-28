package in.abdulmajid.cardiq.auth.controller;

import in.abdulmajid.cardiq.auth.dto.AuthResponse;
import in.abdulmajid.cardiq.auth.dto.LoginRequest;
import in.abdulmajid.cardiq.auth.dto.SignupRequest;
import in.abdulmajid.cardiq.auth.dto.UpdateProfileRequest;
import in.abdulmajid.cardiq.auth.dto.UserProfileResponse;
import in.abdulmajid.cardiq.auth.entity.User;
import in.abdulmajid.cardiq.auth.service.AuthService;
import in.abdulmajid.cardiq.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<AuthResponse> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("User registered successfully")
                .data(authService.signup(request))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successful")
                .data(authService.login(request))
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getProfile(
            @AuthenticationPrincipal User user
    ) {
        return ApiResponse.<UserProfileResponse>builder()
                .success(true)
                .message("Profile fetched successfully")
                .data(authService.getProfile(user))
                .build();
    }

    @GetMapping("/verify-email")
    public ApiResponse<Void> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Email verified successfully")
                .build();
    }

    @PostMapping("/resend-verification")
    public ApiResponse<Void> resendVerification(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ApiResponse.<Void>builder()
                    .success(false)
                    .message("Email is required")
                    .build();
        }
        authService.resendVerification(email);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("Verification email sent")
                .build();
    }

    @PutMapping("/profile")
    public ApiResponse<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ApiResponse.<UserProfileResponse>builder()
                .success(true)
                .message("Profile updated successfully")
                .data(authService.updateProfile(user, request))
                .build();
    }

    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ApiResponse.<Void>builder()
                    .success(false)
                    .message("Email is required")
                    .build();
        }
        authService.forgotPassword(email);
        return ApiResponse.<Void>builder()
                .success(true)
                .message("If the email exists and is verified, a reset link has been sent")
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String password = body.get("password");
        if (token == null || token.isBlank() || password == null || password.isBlank()) {
            return ApiResponse.<Void>builder()
                    .success(false)
                    .message("Token and password are required")
                    .build();
        }
        try {
            authService.resetPassword(token, password);
            return ApiResponse.<Void>builder()
                    .success(true)
                    .message("Password has been reset successfully")
                    .build();
        } catch (RuntimeException e) {
            return ApiResponse.<Void>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }
}
