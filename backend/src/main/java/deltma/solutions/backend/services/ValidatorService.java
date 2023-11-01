package deltma.solutions.backend.services;

import deltma.solutions.backend.models.Role;
import deltma.solutions.backend.models.User;

public interface ValidatorService {

    void validateUser(User user);

    void validateString(String input, String fieldName);

    void validateInteger(Integer input, String fieldName);

    void validateEmail(String email);

    void validatePassword(String password);

    void validatePhoneNumber(String phoneNumber);

    void validateName(String name);

    void validateRole(Role role);
}
