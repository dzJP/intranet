package deltma.solutions.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendInvitation(List<String> emails, String invitationLink) {
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
}
