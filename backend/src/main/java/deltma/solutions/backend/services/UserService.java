package deltma.solutions.backend.services;

import deltma.solutions.backend.dto.SignUpRequest;
import deltma.solutions.backend.models.Role;
import deltma.solutions.backend.models.User;
import deltma.solutions.backend.repositories.UserRepository;
import deltma.solutions.backend.utils.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * User authentication
 **/
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ValidatorService validatorService;
    private final PasswordGenerator passwordGenerator;
    private final EmailService emailService;

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByEmail(username).orElseThrow(() ->
                        new UsernameNotFoundException("Username not found"));
            }
        };
    }

    public void validateUser(SignUpRequest request) {
        validatorService.validateEmail(request.getEmail());
        validatorService.validateName(request.getFirstName());
        validatorService.validateName(request.getLastName());
        validatorService.validatePassword(request.getPassword());
        validatorService.validatePhoneNumber(request.getPhoneNumber());
    }

    // Create a new User object using builder pattern with provided details.
    public User createAndSaveUser(SignUpRequest request) {

        validateUser(request);
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

    public boolean isEmailAssociatedWithUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void resetPasswordAndSendEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        String newPassword = passwordGenerator.generateRandomPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        emailService.sendNewPassword(email, newPassword);
    }

}