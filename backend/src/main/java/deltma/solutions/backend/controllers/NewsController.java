package deltma.solutions.backend.controllers;

import deltma.solutions.backend.dto.NewsDTO;
import deltma.solutions.backend.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-news")
    public ResponseEntity<String> createNewsArticle(@RequestBody NewsDTO newsDTO) {
        try {
            newsService.createNewsArticle(newsDTO);
            return ResponseEntity.ok("News article created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating news article " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-news")
    public ResponseEntity<List<NewsDTO>> getNews() {
        // Assume newsService is a service class that fetches news articles
        List<NewsDTO> newsList = newsService.getNews();
        return ResponseEntity.ok(newsList);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/latest-news")
    public ResponseEntity<List<NewsDTO>> getLatestNews() {
        try {
            List<NewsDTO> latestNews = newsService.getLatestNews();
            return ResponseEntity.ok(latestNews);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }
}
