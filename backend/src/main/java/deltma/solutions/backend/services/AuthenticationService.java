package deltma.solutions.backend.services;

import deltma.solutions.backend.dto.JwtAuthenticationResponse;
import deltma.solutions.backend.dto.SignInRequest;
import deltma.solutions.backend.dto.SignUpRequest;
import deltma.solutions.backend.models.Role;
import deltma.solutions.backend.models.User;
import deltma.solutions.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // SignUpRequest is used to create a new user account.
    public JwtAuthenticationResponse signup(SignUpRequest request) {

        // Create a new User object using builder pattern with provided details.
        var user = User.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword())) // Encode the password.
                .phoneNumber(request.getPhoneNumber())
                .role(Role.ROLE_USER) // Assign a role to the user (e.g., ROLE_USER).
                .build();

        // Save the user using the UserService and get the saved user with assigned ID.
        user = userService.save(user);

        // Generate a JWT token for the newly registered user.
        var jwt = jwtService.generateToken(user);

        // Return a JwtAuthenticationResponse containing the generated token.
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    // SignInRequest is used to authenticate an existing user.
    public JwtAuthenticationResponse signin(SignInRequest request) {
        // Attempt to authenticate the user using the provided email and password.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // Retrieve the user from the UserRepository based on the provided email.
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        // Generate a JWT token for the authenticated user.
        var jwt = jwtService.generateToken(user);

        // Retrieve the user's role from the User entity.
        Role role = user.getRole();
        System.out.println(role.toString());

        // Return a JwtAuthenticationResponse containing the generated token.
        return JwtAuthenticationResponse
                .builder()
                .token(jwt)
                .role(role.toString())
                .build();
    }



}
