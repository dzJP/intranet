package deltma.solutions.backend.services;

import deltma.solutions.backend.models.Role;
import deltma.solutions.backend.models.User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ValidatorService {



    public void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null");
        }
    }
    // Validate email format
    public void validateEmail(String email) {
        if (email == null || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }
    public boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(regex);
    }

    public Set<String> validateEmails(Set<String> emails) {
        Set<String> validEmails = new HashSet<>();
        for (String email : emails) {
            if (isValidEmail(email)) {
                validEmails.add(email);
            }
        }
        return validEmails;
    }
    // Validate so name contains only alphabetic characters
    public void validateName(String name) {
        if (!name.matches("^[a-zA-Z]+$") || name.length() < 2 || name.length() > 30) {
            throw new IllegalArgumentException("Invalid name format");
        }
    }

    // Password criteria: at least 8 characters, 1 digit, 1 uppercase letter, 1 special character
    public void validatePassword(String password) {
        if (!password.matches("^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$")) {
            throw new IllegalArgumentException("Invalid password format");
        }
    }
    // Phone number is not null and is not empty
    public void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
    }

    // Validate if Role is valid
    public void validateRole(Role role) {
        if (role != Role.ROLE_ADMIN && role != Role.ROLE_USER) {
            throw new IllegalArgumentException("Invalid role");
        }
    }

    // Input is not null or empty
    public void validateString(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }

    // Input is not null
    public void validateInteger(Integer input, String fieldName) {
        if (input == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }



    // more
}

