package deltma.solutions.backend.repositories;

import deltma.solutions.backend.models.TemporaryUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TemporaryUserRepository extends JpaRepository <TemporaryUser, String> {

    Optional<TemporaryUser> findByUuid(UUID uuid);

    Optional<TemporaryUser> findTempUserByEmail(String email);

}
