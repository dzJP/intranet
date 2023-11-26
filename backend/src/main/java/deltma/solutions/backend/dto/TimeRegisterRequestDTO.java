package deltma.solutions.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TimeRegisterRequestDTO {

    private String email;
    private String workHours;
    private LocalDate date;

    public TimeRegisterRequestDTO(String workHours, LocalDate date, String email) {
        this.workHours = workHours;
        this.date = date;
        this.email = email;

    }
}