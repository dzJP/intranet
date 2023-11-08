package deltma.solutions.backend.services;

import deltma.solutions.backend.dto.TemporaryUserDTO;
import deltma.solutions.backend.models.TemporaryUser;
import deltma.solutions.backend.models.User;
import deltma.solutions.backend.repositories.TemporaryUserRepository;
import deltma.solutions.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TemporaryUserService {

    private final UserRepository userRepository;
    private final TemporaryUserRepository temporaryUserRepository;
    private final EmailService emailService;
    private final ValidatorService validatorService;


    public TemporaryUserService(UserRepository userRepository, TemporaryUserRepository temporaryUserRepository, EmailService emailService, ValidatorService validatorService) {
        this.userRepository = userRepository;
        this.temporaryUserRepository = temporaryUserRepository;
        this.emailService = emailService;
        this.validatorService = validatorService;
    }

    public void validateAndSendInvitations(Set<String> validEmails) {
        Set<String> validatedEmails = validatorService.validateEmails(validEmails); // Validate all emails
        Set<String> associatedEmails = isEmailAssociated(validatedEmails); // Check email associations

        for (String email : associatedEmails) {
            validatorService.validateEmail(email); // Validate individual email

            UUID uuid = UUID.randomUUID();
            TemporaryUser temporaryUser = new TemporaryUser(email, uuid.toString());
            temporaryUserRepository.save(temporaryUser);

            String invitationLink = generateInvitationLink(uuid);
            emailService.sendInvitation(email, invitationLink);
        }
    }
    public Set<String> isEmailAssociated(Set<String> emails) {
        Set<String> associatedEmails = new HashSet<>();
        for (String email : emails) {
            Optional<User> userOptional = userRepository.findByEmail(email);
            Optional<TemporaryUser> tempUserOptional = temporaryUserRepository.findByEmail(email);
            if (userOptional.isPresent() || tempUserOptional.isPresent()) {
                associatedEmails.add(email);
            }
        }
        return associatedEmails;
    }

    private String generateInvitationLink(UUID uuid) {
        return "http://localhost:8081/register/" + uuid;
    }

    public Optional<TemporaryUserDTO> findTempUserByUuid(String uuid) {
        Optional<TemporaryUser> temporaryUserOptional = temporaryUserRepository.findByUuid(uuid);
        return temporaryUserOptional.map(temporaryUser ->
                new TemporaryUserDTO(Collections.singleton(temporaryUser.getEmail()), temporaryUser.getUuid()));
    }

    public Optional<TemporaryUserDTO> findTempUserByEmail(String email) {
        Optional<TemporaryUser> temporaryUserOptional = temporaryUserRepository.findByEmail(email);
        return temporaryUserOptional.map(temporaryUser -> new TemporaryUserDTO(Collections.singleton(temporaryUser.getEmail()), temporaryUser.getUuid()));
    }

    public void deleteTemporaryUserByEmail(String email) {
        temporaryUserRepository.findByEmail(email).ifPresent(temporaryUser -> {
            temporaryUserRepository.delete(temporaryUser);
        });
    }

}


