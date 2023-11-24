package deltma.solutions.backend.repositories;

import deltma.solutions.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}