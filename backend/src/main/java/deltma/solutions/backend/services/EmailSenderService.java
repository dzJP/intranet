package deltma.solutions.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    // Method to send an email
    public void sendEmail(String toEmail, String subject, String body) {
        // Creating a new SimpleMailMessage object
        SimpleMailMessage message = new SimpleMailMessage();

        // Setting the sender's email address
        message.setFrom("noreply@deltmasolutions.com");

        // Setting the recipient's email address
        message.setTo(toEmail);

        // Setting the body of the email
        message.setText(body);

        // Setting the subject of the email
        message.setSubject(subject);

        // Sending the email using the JavaMailSender
        mailSender.send(message);

        System.out.println("Mail Sent Successfully...");
    }
}
