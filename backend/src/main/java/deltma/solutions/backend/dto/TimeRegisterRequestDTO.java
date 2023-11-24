package deltma.solutions.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TimeRegisterRequestDTO {

    private String email;
    private Double hoursWorked;
    private LocalDate date;
}