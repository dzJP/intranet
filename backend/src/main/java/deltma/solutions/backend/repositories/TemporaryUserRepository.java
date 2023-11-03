package deltma.solutions.backend.repositories;

import deltma.solutions.backend.models.TemporaryUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TemporaryUserRepository extends JpaRepository<TemporaryUser, String> {
    Optional<TemporaryUser> findByUuid(UUID uuid);
    Optional<TemporaryUser> findByEmail(String email);
}

