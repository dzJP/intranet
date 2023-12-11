package deltma.solutions.backend.services;

import deltma.solutions.backend.dto.NewsDTO;
import deltma.solutions.backend.models.News;
import deltma.solutions.backend.repositories.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {

    @Autowired
    private final NewsRepository newsRepository;

    public void createNewsArticle(NewsDTO newsDTO) {
        News news = new News(
                newsDTO.getSubject(),
                newsDTO.getMessage(),
                newsDTO.getDate(),
                newsDTO.getDeadline()
        );
        newsRepository.save(news);
    }

    public List<NewsDTO> getNews() {
        // Retrieve all news
        List<News> newsList = newsRepository.findAll();

        // Convert the list of News entities to a list of NewsDTO objects
        return newsList.stream()
                .map(news -> {
                    NewsDTO newsDTO = new NewsDTO();
                    newsDTO.setSubject(news.getSubject());
                    newsDTO.setMessage(news.getMessage());
                    newsDTO.setDate(news.getDate());
                    newsDTO.setDeadline(news.getDeadline());

                    return newsDTO;
                })
                .collect(Collectors.toList());
    }
}
