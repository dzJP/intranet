package deltma.solutions.backend.repositories;
import deltma.solutions.backend.models.TimeRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeRegisterRepository extends JpaRepository<TimeRegister, Long> {

    @Query("SELECT tr FROM TimeRegister tr WHERE YEAR(tr.date) = :year AND MONTH(tr.date) = :month")
    List<TimeRegister> findByDateYearAndDateMonth(@Param("year") int year, @Param("month") int month);

}
