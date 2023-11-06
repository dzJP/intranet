package deltma.solutions.backend.services;

import deltma.solutions.backend.dto.TemporaryUserDTO;
import deltma.solutions.backend.models.TemporaryUser;
import deltma.solutions.backend.models.User;
import deltma.solutions.backend.repositories.TemporaryUserRepository;
import deltma.solutions.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TemporaryUserService {

    private final UserRepository userRepository;
    private final TemporaryUserRepository temporaryUserRepository;
    private final EmailService emailService;

    public TemporaryUserService(UserRepository userRepository, TemporaryUserRepository temporaryUserRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.temporaryUserRepository = temporaryUserRepository;
        this.emailService = emailService;
    }

    public void inviteAndSaveTemporaryUsers(List<String> emails) {
        for (String email : emails) {
            UUID uuid = UUID.randomUUID();
            TemporaryUser temporaryUser = new TemporaryUser(email, uuid.toString());
            temporaryUserRepository.save(temporaryUser);

            String invitationLink = generateInvitationLink(uuid);
            emailService.sendInvitation(List.of(email), invitationLink);
        }
    }

    private String generateInvitationLink(UUID uuid) {
        return "http://localhost:8081/register/" + uuid;
    }

    public Optional<TemporaryUserDTO> findTempUserByUuid(String uuid) {
        Optional<TemporaryUser> temporaryUserOptional = temporaryUserRepository.findByUuid(uuid);
        return temporaryUserOptional.map(temporaryUser ->
                new TemporaryUserDTO(temporaryUser.getEmail(), temporaryUser.getUuid()));
    }

    public Optional<TemporaryUserDTO> findTempUserByEmail(String email) {
        Optional<TemporaryUser> temporaryUserOptional = temporaryUserRepository.findByEmail(email);
        return temporaryUserOptional.map(temporaryUser -> new TemporaryUserDTO(temporaryUser.getEmail(), temporaryUser.getUuid()));
    }

    public boolean isEmailAssociated(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        Optional<TemporaryUser> tempUserOptional = temporaryUserRepository.findByEmail(email);
        return userOptional.isPresent() || tempUserOptional.isPresent();
    }

}


