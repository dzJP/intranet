package deltma.solutions.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TimeRegisterRequestDTO {

    private String email;
    private Integer workHours;
    private LocalDate date;

    public TimeRegisterRequestDTO(Integer workHours, LocalDate date, String email) {
        this.workHours = workHours;
        this.date = date;
        this.email = email;

    }

    public int getMonth() {
        return date.getMonthValue();
    }

    public int getYear() {
        return date.getYear();
    }
}