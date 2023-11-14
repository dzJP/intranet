package deltma.solutions.backend.services;

import deltma.solutions.backend.dto.SignUpRequest;
import deltma.solutions.backend.dto.UserProfileDTO;
import deltma.solutions.backend.models.Role;
import deltma.solutions.backend.models.User;
import deltma.solutions.backend.repositories.UserRepository;
import deltma.solutions.backend.utils.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * User authentication
 **/
@Service
@RequiredArgsConstructor
public class UserService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ValidatorService validatorService;
    private final EmailService emailService;
    private final PasswordGenerator passwordGenerator;

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByEmail(username).orElseThrow(() ->
                        new UsernameNotFoundException("Username not found"));
            }
        };
    }

    public void createDefaultUsers() {
        User admin1 = User
                .builder()
                .email("admin1@admin.com")
                .firstName("admin")
                .lastName("admin")
                .password(passwordEncoder.encode("password"))
                .phoneNumber("123456789")
                .role(Role.ROLE_ADMIN)
                .isActive(true)
                .build();

        save(admin1);
        System.out.println("created ADMIN user " + admin1);

        User user1 = User
                .builder()
                .email("user1@user.com")
                .firstName("user")
                .lastName("user")
                .password(passwordEncoder.encode("password"))
                .phoneNumber("123456789")
                .role(Role.ROLE_USER)
                .isActive(true)
                .build();

        save(user1);
        System.out.println("created USER user " + user1);

        // test remove later
//        User testuser = User
//                .builder()
//                .email("jakob.pietrzyk@deltmasolutions.com")
//                .firstName("testuser")
//                .lastName("testuser")
//                .password(passwordEncoder.encode("password"))
//                .phoneNumber("1234567890")
//                .role(Role.ROLE_ADMIN)
//                .isActive(true)
//                .build();
//
//        save(testuser);
    }

    // Create a new User object using builder pattern with provided details.
    public User createAndSaveUser(SignUpRequest request) {
        validatorService.validateUser(request);

        var user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.ROLE_USER)
                .isActive(true)
                .build();

        return userRepository.save(user);
    }

    // Generate a JWT token for the user.
    public String generateJwtToken(User user) {
        return jwtService.generateToken(user);
    }

    public User save(User newUser) {
        return userRepository.save(newUser);
    }

    public UserProfileDTO getUserProfileByEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserProfileDTO(user.getEmail(), user.getFirstName()
                , user.getLastName(), user.getPhoneNumber());
    }

    public UserProfileDTO getUserProfileByUsername(String username) {
        // Validation using the ValidatorService
        if (validatorService.isValidEmail(username)) {
            // Retrieve the user profile
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return new UserProfileDTO(user.getEmail(), user.getFirstName(),
                    user.getLastName(), user.getPhoneNumber());
        } else {
            return null;
        }
    }

    public void updatePhoneNumber(UserProfileDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);
    }

    public boolean isEmailAssociatedWithUser(String email)   {
        return userRepository.findByEmail(email).isPresent();
    }

    public void resetUserPassword(String userEmail) {
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isPresent()) {
            //emailService.sendPasswordResetLink(userEmail);
            User user = userOptional.get();
            String newPassword = passwordGenerator.generateRandomPassword();
            changeUserPassword(user, newPassword);
        } else {
            System.out.println("Email is not associated with a user");

        }
    }

    private void changeUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        emailService.sendNewPassword(user.getEmail(), newPassword);
    }

    public List<UserProfileDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserProfileDTO(user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhoneNumber()))
                .collect(Collectors.toList());
    }

    public void editUser(String email, UserProfileDTO request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        validatorService.validateUserProfile(request);
        
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());

        userRepository.save(user);
    }

    public void deleteUser(String email) {
        userRepository.findByEmail(email).ifPresent(user -> userRepository.delete(user));
    }

    @Override
    public void run(String... args) throws Exception {
        createDefaultUsers();
    }

}
