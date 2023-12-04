package deltma.solutions.backend.repositories;

import deltma.solutions.backend.models.MonthlyTimeRegister;
import deltma.solutions.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MonthlyTimeRegisterRepository extends JpaRepository<MonthlyTimeRegister, Long> {

    List<MonthlyTimeRegister> findByUserAndYearAndMonth(User user, int year, int month);

    List<MonthlyTimeRegister> findByUserAndYear(User user, int year);

    @Transactional
    void deleteByUser(User user);
}