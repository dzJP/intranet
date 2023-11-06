package deltma.solutions.backend.repositories;

import deltma.solutions.backend.models.TemporaryUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemporaryUserRepository extends JpaRepository<TemporaryUser, String> {
    Optional<TemporaryUser> findByUuid(String uuid);
    Optional<TemporaryUser> findByEmail(String email);
}

