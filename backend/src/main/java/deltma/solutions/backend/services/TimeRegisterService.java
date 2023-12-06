package deltma.solutions.backend.services;

import deltma.solutions.backend.dto.TimeRegisterRequestDTO;
import deltma.solutions.backend.models.TimeRegister;
import deltma.solutions.backend.models.User;
import deltma.solutions.backend.repositories.TimeRegisterRepository;
import deltma.solutions.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeRegisterService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TimeRegisterRepository timeRegisterRepository;

    public void registerTime(TimeRegisterRequestDTO timeRegisterRequestDTO) {
        User user = userRepository.findByEmail(timeRegisterRequestDTO.getEmail());

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        TimeRegister timeRegister = new TimeRegister();
        timeRegister.setWorkHours(timeRegisterRequestDTO.getWorkHours());
        timeRegister.setDate(timeRegisterRequestDTO.getDate());
        timeRegister.setUser(user);

        timeRegisterRepository.save(timeRegister);
    }

    public List<TimeRegisterRequestDTO> getFormerRegistrationsThisMonthForAllUsers(String email) {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();

        List<TimeRegister> timeRegistrations = timeRegisterRepository.findByUserEmailAndDateYearAndDateMonth(
                email, currentYear, currentMonth);

        return timeRegistrations.stream()
                .map(timeRegister -> new TimeRegisterRequestDTO(
                        timeRegister.getWorkHours(),
                        timeRegister.getDate(),
                        timeRegister.getUser().getEmail()
                ))
                .collect(Collectors.toList());
    }

    public Integer getTotalTimeForCurrentMonth(String userEmail) {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();

        List<TimeRegister> timeRegistrations = timeRegisterRepository.findByUserEmailAndDateYearAndDateMonth(
                userEmail, currentYear, currentMonth);

        return timeRegistrations.stream()
                .mapToInt(TimeRegister::getWorkHours)
                .sum();
    }

}

