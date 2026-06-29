package in.abdulmajid.fau.config;

import in.abdulmajid.fau.auth.entity.User;
import in.abdulmajid.fau.auth.enums.UserRole;
import in.abdulmajid.fau.auth.repository.UserRepository;
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
        if (userRepository.existsByEmailIgnoreCase("admin@abdulmajid.in")) {
            log.info("Admin user already exists");
            return;
        }

        User admin = new User();
        admin.setName("Admin");
        admin.setEmail("admin@abdulmajid.in");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(UserRole.ADMIN);
        admin.setActive(true);

        userRepository.save(admin);
        log.info("Default admin user created: admin@abdulmajid.in / admin123");
    }
}
