package deltma.solutions.backend.services;

import deltma.solutions.backend.dto.PhoneNumberUpdateDTO;
import deltma.solutions.backend.dto.SignUpRequest;
import deltma.solutions.backend.dto.UserProfileDTO;
import deltma.solutions.backend.models.Role;
import deltma.solutions.backend.models.User;
import deltma.solutions.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByEmail(username).orElseThrow(() ->
                        new UsernameNotFoundException("Username not found"));
            }
        };
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
        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserProfileDTO(user.getEmail(), user.getFirstName()
                , user.getLastName(), user.getPhoneNumber());
    }

    public void updatePhoneNumber(PhoneNumberUpdateDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);
    }

    public boolean isEmailAssociatedWithUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}