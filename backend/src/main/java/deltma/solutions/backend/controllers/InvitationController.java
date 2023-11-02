package deltma.solutions.backend.controllers;

import deltma.solutions.backend.services.EmailService;
import deltma.solutions.backend.services.TemporaryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:8081")
public class InvitationController {

    private final EmailService emailService;

    public InvitationController(EmailService emailService) {
        this.emailService = emailService;
    }

    private String generateInvitationLink(UUID uuid) {
        return "http://localhost:8081/register/" + uuid;
    }

    @PostMapping("/send-invitations")
    public ResponseEntity<String> sendInvitations(@RequestBody List<String> emails) {
        System.out.println("Received request to send invitations: " + emails);
        try {
            for (String email : emails) {
                emailService.sendInvitations(emails, generateInvitationLink(UUID.randomUUID()));
            }
            return ResponseEntity.ok("Invitations sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending invitations: " + e.getMessage());
        }
    }
}