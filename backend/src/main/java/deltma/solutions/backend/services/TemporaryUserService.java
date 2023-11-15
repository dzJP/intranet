package deltma.solutions.backend.services;

import deltma.solutions.backend.dto.TemporaryUserDTO;
import deltma.solutions.backend.models.TemporaryUser;
import deltma.solutions.backend.repositories.TemporaryUserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TemporaryUserService {

    private final TemporaryUserRepository temporaryUserRepository;
    private final EmailService emailService;
    private final ValidatorService validatorService;

    public TemporaryUserService(TemporaryUserRepository temporaryUserRepository, EmailService emailService, ValidatorService validatorService) {
        this.temporaryUserRepository = temporaryUserRepository;
        this.emailService = emailService;
        this.validatorService = validatorService;
    }

    public void validateAndSendInvitations (TemporaryUserDTO temporaryUserDTO) {

        Set<String> validatedEmails = validatorService.validateEmails(temporaryUserDTO.getEmails());

        for (String email : validatedEmails) {
            if(!isEmailAssociated(email)) {
                TemporaryUser temporaryUser = new TemporaryUser(email, UUID.randomUUID().toString());
                temporaryUserRepository.save(temporaryUser);

                String invitationLink = generateInvitationLink(UUID.fromString(temporaryUser.getUuid()));
                emailService.sendInvitation(email, invitationLink);
            }
        }
    }

    public boolean isEmailAssociated(String email) {
        return temporaryUserRepository.findByEmail(email).isPresent();
    }

    private String generateInvitationLink(UUID uuid) {
        return "http://localhost:8081/register/" + uuid;
    }

    public Optional<TemporaryUserDTO> findTempUserByUuid(String uuid) {
        Optional<TemporaryUser> temporaryUserOptional = temporaryUserRepository.findByUuid(uuid);
        return temporaryUserOptional.map(temporaryUser ->
                new TemporaryUserDTO(Collections.singleton(temporaryUser.getEmail()), temporaryUser.getUuid()));
    }

    public void deleteTemporaryUserByEmail(String email) {
        temporaryUserRepository.findByEmail(email).ifPresent(temporaryUserRepository::delete);
    }

}