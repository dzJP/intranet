package deltma.solutions.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendInvitation(Set<String> emails, String invitationLink) {
        for (String email : emails) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("noreply@deltmasolutions.com");
                message.setTo(email);
                message.setSubject("Invitation to Register");
                message.setText("Hello!\n\nYou are invited to register on our platform. Use the following link to register: "
                        + invitationLink);

                javaMailSender.send(message);

                System.out.println("Invitation sent to: " + email);
            } catch (Exception e) {
                System.err.println("Error sending invitation to: " + email);
                e.printStackTrace(); // Handle or log the error
            }
        }
    }

    public void sendNewPassword(String email, String newPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@deltmasolutions.com");
            message.setTo(email);
            message.setSubject("New Password");
            message.setText("Hello!\n\nYour new password is: " + newPassword);

            javaMailSender.send(message);

            System.out.println("New password sent to: " + email);
        } catch (Exception e) {
            System.err.println("Error sending new password to: " + email);
            e.printStackTrace(); // Handle or log the error
        }
    }
}
