package deltma.solutions.backend.repositories;

import deltma.solutions.backend.models.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findTopNByOrderByDateDesc();
}
