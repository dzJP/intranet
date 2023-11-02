package deltma.solutions.backend.controllers;

import deltma.solutions.backend.dto.TemporaryUserDTO;
import deltma.solutions.backend.services.TemporaryUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final TemporaryUserService temporaryUserService;

    public RegistrationController(TemporaryUserService temporaryUserService) {
        this.temporaryUserService = temporaryUserService;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> registerUser(@PathVariable UUID uuid) {
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
}
