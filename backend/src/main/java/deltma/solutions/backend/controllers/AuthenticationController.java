package deltma.solutions.backend.controllers;

import deltma.solutions.backend.dto.JwtAuthenticationResponse;
import deltma.solutions.backend.dto.SignInRequest;
import deltma.solutions.backend.dto.SignUpRequest;
import deltma.solutions.backend.dto.TemporaryUserDTO;
import deltma.solutions.backend.services.AuthenticationService;
import deltma.solutions.backend.services.TemporaryUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/** Responsible for managing HTTP requests associated with user authentication. **/
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final TemporaryUserService temporaryUserService;

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
    public ResponseEntity<String> sendInvitations(@RequestBody List<String> emails) {
        for (String email : emails) {
            boolean isAssociated = temporaryUserService.isEmailAssociated(email);
            if (isAssociated) {
                throw new RuntimeException("Email " + email + " is associated with an existing user.");
            }
        }

        try {
            temporaryUserService.inviteAndSaveTemporaryUsers(emails);
            return ResponseEntity.ok("Invitations sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending invitations: " + e.getMessage());
        }
    }

}