package deltma.solutions.backend.controllers;

import deltma.solutions.backend.models.User;
import deltma.solutions.backend.services.ValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private ValidatorService validatorService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            validatorService.validateUser(user);
            validatorService.validatePassword(user.getPassword());
            validatorService.validatePhoneNumber(user.getPhoneNumber());
            validatorService.validateName(user.getFirstName());
            validatorService.validateName(user.getLastName());
            validatorService.validateRole(user.getRole());

            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
