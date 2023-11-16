package deltma.solutions.backend.controllers;

import deltma.solutions.backend.dto.*;
import deltma.solutions.backend.services.AuthenticationService;
import deltma.solutions.backend.services.TemporaryUserService;
import deltma.solutions.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
            TemporaryUserDTO temporaryUserDTO = new TemporaryUserDTO(Collections.singleton(uuid), uuid);
            temporaryUserService.validateAndSendInvitations(temporaryUserDTO);

            // Attempt to find the temporary user by UUID
            TemporaryUserDTO foundUser = temporaryUserService.findTempUserByUuid(uuid);

            // Return a response, whether found or not
            return foundUser != null ? ResponseEntity.ok(foundUser) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user.");
        }
    }

    @PostMapping("/send-invitations")
    public ResponseEntity<String> sendInvitations(@RequestBody Set<String> emails, TemporaryUserDTO temporaryUserDTO) {
        try {
            temporaryUserService.validateAndSendInvitations(new TemporaryUserDTO(emails, temporaryUserDTO.getUuid()));
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
    public ResponseEntity<?> updatePhoneNumber(@RequestBody UserProfileDTO request) {
        try {
            userService.updatePhoneNumber(request);
            return ResponseEntity.ok("Phone number updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating phone number: " + e.getMessage());
        }
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String email) {
        try {
            UserProfileDTO userProfileDTO = userService.getUserProfileByUsername(email);
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

    @GetMapping("/admin")
    public ResponseEntity<?> getUsers() {
        try {
            List<UserProfileDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving users: " + e.getMessage());
        }
    }

    @PutMapping("/admin/edit-user/{email}")
    public ResponseEntity<?> editUser(@PathVariable String email, @RequestBody UserProfileDTO request) {
        try {
            userService.editUser(email,request);
            return ResponseEntity.ok("User successfully edited");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error editing user: " + e.getMessage());
        }
    }

    @DeleteMapping("/admin/delete-user/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        try {
            userService.deleteUser(email);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting user: " + e.getMessage());
        }
    }

    @PatchMapping("/admin/deactivate-user/{email}")
    public ResponseEntity<?> deactivateUser(@PathVariable String email) {
        try {
            userService.deactivateUser(email);
            return ResponseEntity.ok("User deactivated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deactivating user: " + e.getMessage());
        }
    }

    @PatchMapping("/admin/activate-user/{email}")
    public ResponseEntity<?> activateUser(@PathVariable String email) {
        try {
            userService.activateUser(email);
            return ResponseEntity.ok("User activated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error activating user: " + e.getMessage());
        }
    }

}
