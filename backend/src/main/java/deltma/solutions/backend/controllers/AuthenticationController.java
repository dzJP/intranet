package deltma.solutions.backend.controllers;

import deltma.solutions.backend.dto.*;
import deltma.solutions.backend.services.AuthenticationService;
import deltma.solutions.backend.services.TemporaryUserService;
import deltma.solutions.backend.services.UserService;
import deltma.solutions.backend.utils.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        try {
            // Validate emails, create temporary users, and send invitations if necessary
            temporaryUserService.validateAndSendInvitations(Set.of(uuid));

            // Attempt to find the temporary user by UUID
            Optional<TemporaryUserDTO> temporaryUserDTOOptional = temporaryUserService.findTempUserByUuid(uuid);

            // Return a response based on whether the temporary user was found
            return temporaryUserDTOOptional
                    .map(ResponseEntity::ok)  // If found, return OK with the user details
                    .orElseGet(() -> ResponseEntity.notFound().build());  // If not found, return Not Found

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user.");
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

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            UserProfileDTO userProfileDTO = userService.getUserProfileByEmail();
            return ResponseEntity.ok(userProfileDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving user profile: " + e.getMessage());
        }
    }


    @PutMapping("/profile/update-phone-number")
    public ResponseEntity<?> updatePhoneNumber(@RequestBody PhoneNumberUpdateDTO request) {
        try {
            userService.updatePhoneNumber(request);
            return ResponseEntity.ok("Phone number updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating phone number: " + e.getMessage());
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            UserProfileDTO userProfileDTO = userService.getUserProfileByUsername(username);
            return ResponseEntity.ok(userProfileDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving user profile by username: " + e.getMessage());
        }
    }

    @GetMapping("/colleagues")
    public ResponseEntity<List<UserProfileDTO>> getAllUsers() {
        try {
            List<UserProfileDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
