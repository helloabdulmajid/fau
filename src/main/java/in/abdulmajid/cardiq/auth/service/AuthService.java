package in.abdulmajid.cardiq.auth.service;

import in.abdulmajid.cardiq.auth.dto.AuthResponse;
import in.abdulmajid.cardiq.auth.dto.LoginRequest;
import in.abdulmajid.cardiq.auth.dto.SignupRequest;
import in.abdulmajid.cardiq.auth.dto.UpdateProfileRequest;
import in.abdulmajid.cardiq.auth.dto.UserProfileResponse;
import in.abdulmajid.cardiq.auth.entity.User;
import in.abdulmajid.cardiq.auth.enums.UserRole;
import in.abdulmajid.cardiq.auth.repository.UserRepository;
import in.abdulmajid.cardiq.auth.security.JwtTokenProvider;
import in.abdulmajid.cardiq.email.EmailService;
import in.abdulmajid.cardiq.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);
        user.setActive(true);
        user.setEmailVerified(false);

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);

        User savedUser = userRepository.save(user);

        sendVerificationEmail(savedUser);

        String token = jwtTokenProvider.generateToken(
                savedUser.getId(),
                savedUser.getEmail()
        );

        return buildAuthResponse(savedUser, token);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().toLowerCase().trim(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtTokenProvider.generateToken(
                user.getId(),
                user.getEmail()
        );

        return buildAuthResponse(user, token);
    }

    public void verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired verification token"));

        if (user.getEmailVerified()) {
            return;
        }

        user.setEmailVerified(true);
        userRepository.save(user);
    }

    public void resendVerification(String email) {
        User user = userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getEmailVerified()) {
            throw new RuntimeException("Email already verified");
        }

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        userRepository.save(user);

        sendVerificationEmail(user);
    }

    public UserProfileResponse getProfile(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .emailVerified(user.getEmailVerified())
                .phone(user.getPhone())
                .bio(user.getBio())
                .city(user.getCity())
                .state(user.getState())
                .dob(user.getDob())
                .occupation(user.getOccupation())
                .annualIncome(user.getAnnualIncome())
                .gender(user.getGender())
                .avatarUrl(user.getAvatarUrl())
                .pincode(user.getPincode())
                .preferredCardType(user.getPreferredCardType())
                .emailNotifications(user.getEmailNotifications())
                .build();
    }

    public UserProfileResponse updateProfile(User user, UpdateProfileRequest request) {
        User managed = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getName() != null && !request.getName().isBlank()) {
            managed.setName(request.getName().trim());
        }
        if (request.getPhone() != null) {
            managed.setPhone(request.getPhone().isBlank() ? null : request.getPhone().trim());
        }
        if (request.getBio() != null) {
            managed.setBio(request.getBio().isBlank() ? null : request.getBio().trim());
        }
        if (request.getCity() != null) {
            managed.setCity(request.getCity().isBlank() ? null : request.getCity().trim());
        }
        if (request.getState() != null) {
            managed.setState(request.getState().isBlank() ? null : request.getState().trim());
        }
        if (request.getDob() != null) {
            managed.setDob(request.getDob());
        }
        if (request.getOccupation() != null) {
            managed.setOccupation(request.getOccupation().isBlank() ? null : request.getOccupation().trim());
        }
        if (request.getAnnualIncome() != null) {
            managed.setAnnualIncome(request.getAnnualIncome().isBlank() ? null : request.getAnnualIncome().trim());
        }
        if (request.getGender() != null) {
            managed.setGender(request.getGender().isBlank() ? null : request.getGender().trim());
        }
        if (request.getAvatarUrl() != null) {
            managed.setAvatarUrl(request.getAvatarUrl().isBlank() ? null : request.getAvatarUrl().trim());
        }
        if (request.getPincode() != null) {
            managed.setPincode(request.getPincode().isBlank() ? null : request.getPincode().trim());
        }
        if (request.getPreferredCardType() != null) {
            managed.setPreferredCardType(request.getPreferredCardType().isBlank() ? null : request.getPreferredCardType().trim());
        }
        if (request.getEmailNotifications() != null) {
            managed.setEmailNotifications(request.getEmailNotifications());
        }

        User saved = userRepository.save(managed);
        return getProfile(saved);
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email.toLowerCase().trim())
                .orElse(null);
        if (user == null) return;
        if (!user.getEmailVerified()) {
            sendVerificationEmail(user);
            return;
        }

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        sendResetPasswordEmail(user);
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

    private void sendVerificationEmail(User user) {
        String verifyUrl = frontendUrl + "/verify-email?token=" + user.getVerificationToken();

        String html = """
            <div style="font-family: Arial, sans-serif; max-width: 480px; margin: 0 auto; padding: 32px;">
                <h1 style="font-size: 24px; margin-bottom: 8px;">Verify your email</h1>
                <p style="color: #52525b; font-size: 15px; line-height: 1.6;">
                    Hi %s,
                </p>
                <p style="color: #52525b; font-size: 15px; line-height: 1.6;">
                    Thanks for signing up! Please verify your email address by clicking the button below.
                </p>
                <a href="%s"
                   style="display: inline-block; margin-top: 16px; padding: 12px 24px;
                          background-color: #18181b; color: #ffffff; text-decoration: none;
                          border-radius: 12px; font-size: 14px; font-weight: 600;">
                    Verify Email
                </a>
                <p style="color: #a1a1aa; font-size: 13px; margin-top: 24px;">
                    If you didn't create an account, you can safely ignore this email.
                </p>
            </div>
            """.formatted(user.getName(), verifyUrl);

        emailService.sendEmail(user.getEmail(), "Verify your email — CardIQ", html);
    }

    private void sendResetPasswordEmail(User user) {
        String resetUrl = frontendUrl + "/reset-password?token=" + user.getResetToken();

        String html = """
            <div style="font-family: Arial, sans-serif; max-width: 480px; margin: 0 auto; padding: 32px;">
                <h1 style="font-size: 24px; margin-bottom: 8px;">Reset your password</h1>
                <p style="color: #52525b; font-size: 15px; line-height: 1.6;">
                    Hi %s,
                </p>
                <p style="color: #52525b; font-size: 15px; line-height: 1.6;">
                    We received a request to reset your password. Click the button below to set a new one.
                    This link expires in 1 hour.
                </p>
                <a href="%s"
                   style="display: inline-block; margin-top: 16px; padding: 12px 24px;
                          background-color: #18181b; color: #ffffff; text-decoration: none;
                          border-radius: 12px; font-size: 14px; font-weight: 600;">
                    Reset Password
                </a>
                <p style="color: #a1a1aa; font-size: 13px; margin-top: 24px;">
                    If you didn't request this, you can safely ignore this email.
                </p>
            </div>
            """.formatted(user.getName(), resetUrl);

        emailService.sendEmail(user.getEmail(), "Reset your password — CardIQ", html);
    }

    private AuthResponse buildAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .emailVerified(user.getEmailVerified())
                .build();
    }
}
