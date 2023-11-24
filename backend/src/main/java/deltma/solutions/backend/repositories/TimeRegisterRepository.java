package deltma.solutions.backend.repositories;
;
import deltma.solutions.backend.models.TimeRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRegisterRepository extends JpaRepository<TimeRegister, Long> {

}
