package deltma.solutions.backend.utils;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.Random;

@Component
public class PasswordGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[{]}|;:',<.>/?";

    private static final int PASSWORD_LENGTH = 12;

    private final Random random = new SecureRandom();

    public String generateRandomPassword() {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(randomIndex));
        }
        return password.toString();
    }
}
