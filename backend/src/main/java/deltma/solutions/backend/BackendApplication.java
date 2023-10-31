package deltma.solutions.backend;

import deltma.solutions.backend.services.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class BackendApplication {

    @Autowired
    private EmailSenderService senderService;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    // Calling the sendEmail method from senderService to send an email
    @EventListener(ApplicationReadyEvent.class)
    public void sendMail() {
        senderService.sendEmail("jakobpietrzyk93@gmail.com",
                "This is subject of the mail",
                "This is body of the email");
    }

}
