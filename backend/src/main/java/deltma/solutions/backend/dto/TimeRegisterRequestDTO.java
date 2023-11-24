package deltma.solutions.backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TimeRegisterRequestDTO {

    private String email;
    private String workHours;
    private LocalDate date;
}