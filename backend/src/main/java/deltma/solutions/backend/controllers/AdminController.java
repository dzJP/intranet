package deltma.solutions.backend.controllers;

import deltma.solutions.backend.dto.ChangePasswordDTO;
import deltma.solutions.backend.dto.TemporaryUserDTO;
import deltma.solutions.backend.dto.UserProfileDTO;
import deltma.solutions.backend.services.TemporaryUserService;
import deltma.solutions.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final TemporaryUserService temporaryUserService;
    private final UserService userService;
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<UserProfileDTO>> getUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/send-invitations")
    public ResponseEntity<String> sendInvitations(@RequestBody Set<String> emails, TemporaryUserDTO temporaryUserDTO) {
        try {
            temporaryUserService.validateAndSendInvitations(new TemporaryUserDTO(emails, temporaryUserDTO.getUuid()));
            return ResponseEntity.ok("Invitations sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error sending invitations: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit-user/{email}")
    public ResponseEntity<String> editUser(@PathVariable String email, @RequestBody UserProfileDTO request) {
        try {
            userService.editUser(email,request);
            return ResponseEntity.ok("User successfully edited");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error editing user: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-user/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        try {
            userService.deleteUser(email);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error deleting user: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/deactivate-user/{email}")
    public ResponseEntity<?> deactivateUser(@PathVariable String email) {
        try {
            userService.deactivateUser(email);
            return ResponseEntity.ok("User deactivated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error deactivating user: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/activate-user/{email}")
    public ResponseEntity<?> activateUser(@PathVariable String email) {
        try {
            userService.activateUser(email);
            return ResponseEntity.ok("User activated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error activating user: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/change-password/{email}")
    public ResponseEntity<?> changePassword(@PathVariable String email, @RequestBody ChangePasswordDTO request) {
        try {
            userService.changePassword(email, request);
            return ResponseEntity.ok("User password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error changing user password: " + e.getMessage());
        }
    }

}