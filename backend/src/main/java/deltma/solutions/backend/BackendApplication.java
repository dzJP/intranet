package deltma.solutions.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);

        Dotenv dotenv = Dotenv.load();
        String dbUrl = dotenv.get("MYSQL_DATABASE");
        String dbUsername = dotenv.get("MYSQL_USER");
        String dbPassword = dotenv.get("MYSQL_PASSWORD");

        String tokenKey = dotenv.get("TOKEN_KEY");

        String mailHost = dotenv.get("MAIL_HOST");
        String mailPort = dotenv.get("MAIL_PORT");
        String mailUsername = dotenv.get("MAIL_USERNAME");
        String mailPassword = dotenv.get("MAIL_PASSWORD");
    }
}
