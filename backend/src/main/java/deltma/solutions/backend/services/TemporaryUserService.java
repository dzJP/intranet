package deltma.solutions.backend.services;

import deltma.solutions.backend.dto.TemporaryUserDTO;
import deltma.solutions.backend.models.TemporaryUser;
import deltma.solutions.backend.repositories.TemporaryUserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TemporaryUserService {

    private final TemporaryUserRepository temporaryUserRepository;
    private final EmailService emailService;
    private JavaMailSender javaMailSender;

    public TemporaryUserService(TemporaryUserRepository temporaryUserRepository, EmailService emailService, JavaMailSender javaMailSender) {
        this.temporaryUserRepository = temporaryUserRepository;
        this.emailService = emailService;
        this.javaMailSender = javaMailSender;
    }

    public Optional<TemporaryUserDTO> findTempUserByEmail(String email) {
        Optional<TemporaryUser> temporaryUserOptional = temporaryUserRepository.findTempUserByEmail(email);
        return temporaryUserOptional.map(temporaryUser -> new TemporaryUserDTO(temporaryUser.getEmail(), temporaryUser.getUuid()));
    }

    public TemporaryUserDTO saveTempUser(String email) {
        TemporaryUser temporaryUser = new TemporaryUser(email);
        temporaryUser = temporaryUserRepository.save(temporaryUser);
        return new TemporaryUserDTO(temporaryUser.getEmail(), temporaryUser.getUuid());
    }

    public Optional<TemporaryUserDTO> findTempUserByUuid(UUID uuid) {
        Optional<TemporaryUser> temporaryUserOptional = temporaryUserRepository.findByUuid(uuid);
        return temporaryUserOptional.map(temporaryUser -> new TemporaryUserDTO(temporaryUser.getEmail(), temporaryUser.getUuid()));
    }

    public void inviteTemporaryUsers(List<String> emails) {
        for (String email : emails) {
            TemporaryUser temporaryUser = new TemporaryUser(email);
            temporaryUser = temporaryUserRepository.save(temporaryUser);

            // Send invitation email
            String invitationLink = generateInvitationLink(temporaryUser.getUuid());
            emailService.sendInvitation(List.of(email), invitationLink);
        }
    }

    private String generateInvitationLink(UUID uuid) {
        return "http://localhost:8081/register/" + uuid;
    }

    public void sendInvitations(List<String> emails) {
        for (String email : emails) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Invitation to Register");
            message.setText("Please register using the following link: https://localhost:8081/register");
            javaMailSender.send(message);
        }
    }

    public boolean isEmailAssociatedWithTemporaryUser(String email) {
        return temporaryUserRepository.findTempUserByEmail(email).isPresent();
    }
}
