package deltma.solutions.backend.repositories;

import deltma.solutions.backend.models.MonthlyTimeRegister;
import deltma.solutions.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlyTimeRegisterRepository extends JpaRepository<MonthlyTimeRegister, Long> {

    List<MonthlyTimeRegister> findByUserAndYearAndMonthBefore(User user, Integer year, Integer month);
}