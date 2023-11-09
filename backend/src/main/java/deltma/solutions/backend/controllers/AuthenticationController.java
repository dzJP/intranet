package deltma.solutions.backend.controllers;

import deltma.solutions.backend.dto.JwtAuthenticationResponse;
import deltma.solutions.backend.dto.SignInRequest;
import deltma.solutions.backend.dto.SignUpRequest;
import deltma.solutions.backend.dto.TemporaryUserDTO;
import deltma.solutions.backend.services.AuthenticationService;
import deltma.solutions.backend.services.TemporaryUserService;
import deltma.solutions.backend.services.UserService;
import deltma.solutions.backend.utils.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

/** Responsible for managing HTTP requests associated with user authentication. **/
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final TemporaryUserService temporaryUserService;
    private final UserService userService;
    private final PasswordGenerator passwordGenerator;

    @PostMapping("/signin")
    public JwtAuthenticationResponse signin(@RequestBody SignInRequest request) {
        return authenticationService.signin(request);
    }

    @PostMapping("/register")
    public JwtAuthenticationResponse signup(@RequestBody SignUpRequest request) {
        return authenticationService.signup(request);
    }

    @GetMapping("/register/{uuid}")
    public ResponseEntity<?> registerUser(@PathVariable String uuid) {
        // Retrieve the temporary user using the UUID
        Optional<TemporaryUserDTO> temporaryUserDTOOptional = temporaryUserService.findTempUserByUuid(uuid);

        if (temporaryUserDTOOptional.isPresent()) {
            TemporaryUserDTO temporaryUserDTO = temporaryUserDTOOptional.get();
            return ResponseEntity.ok(temporaryUserDTO);
        } else {
            // Handle the case where the UUID is not valid
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/send-invitations")
    public ResponseEntity<String> sendInvitations(@RequestBody Set<String> emails) {
        try {
            temporaryUserService.validateAndSendInvitations(emails);
            return ResponseEntity.ok("Invitations sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending invitations: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email) {
        try {
            // Reset the user's password and send the new password via email
            userService.resetUserPassword(email);

            return ResponseEntity.ok("New password sent successfully.");
        } catch (Exception e) {
            // Handle any exceptions that may occur (e.g., user not found, email sending failed, etc.)
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error resetting password.");
        }
    }
}