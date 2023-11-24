package deltma.solutions.backend.controllers;

import deltma.solutions.backend.dto.TimeRegisterRequestDTO;
import deltma.solutions.backend.models.TimeRegister;
import deltma.solutions.backend.repositories.TimeRegisterRepository;
import deltma.solutions.backend.services.TimeRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
public class TimeRegisterController {

    @Autowired
    private TimeRegisterService timeRegisterService;

    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
    @PostMapping("/register-time")
    public ResponseEntity<String> registerTime(@RequestBody TimeRegisterRequestDTO timeRegisterRequestDTO) {
        try {
            System.out.println("Received request to register time: " + timeRegisterRequestDTO);

            timeRegisterService.registerTime(timeRegisterRequestDTO);
            return ResponseEntity.ok("Time registered successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error registering time " + e.getMessage());
        }
    }
}
