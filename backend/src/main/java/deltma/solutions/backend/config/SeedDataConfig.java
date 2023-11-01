package deltma.solutions.backend.config;

import deltma.solutions.backend.models.Role;
import deltma.solutions.backend.models.User;
import deltma.solutions.backend.repositories.UserRepository;
import deltma.solutions.backend.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/*
 * Den här klassen är bara för att testa att lägga till användare/admin i databasen.
 * Kommer tas bort senare. */

/** Responsible for populating seed data in the application during initialization. **/
@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {

        User admin1 = User
                .builder()
                .email("admin1@admin.com")
                .firstName("admin1")
                .lastName("admin1")
                .password(passwordEncoder.encode("password"))
                .phoneNumber("123456789")
                .role(Role.ROLE_ADMIN)
                .isActive(true)
                .build();

        userService.save(admin1);
        log.debug("created ADMIN user - {}", admin1);

        User user1 = User
                .builder()
                .email("user1@user.com")
                .firstName("user1")
                .lastName("user1")
                .password(passwordEncoder.encode("password"))
                .phoneNumber("123456789")
                .role(Role.ROLE_USER)
                .isActive(false)
                .build();

        userService.save(user1);
        log.debug("created User user - {}", user1);

    }
}
