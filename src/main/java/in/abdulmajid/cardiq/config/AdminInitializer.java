package in.abdulmajid.cardiq.config;

import in.abdulmajid.cardiq.auth.entity.User;
import in.abdulmajid.cardiq.auth.enums.UserRole;
import in.abdulmajid.cardiq.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.existsByEmailIgnoreCase("admin@cardiq.com")) {
            log.info("Admin user already exists");
            return;
        }

        User admin = new User();
        admin.setName("Admin");
        admin.setEmail("admin@cardiq.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(UserRole.ADMIN);
        admin.setActive(true);

        userRepository.save(admin);
        log.info("Default admin user created: admin@cardiq.com / admin123");
    }
}
