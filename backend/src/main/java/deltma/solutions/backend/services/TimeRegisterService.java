package deltma.solutions.backend.services;

import deltma.solutions.backend.dto.TimeRegisterRequestDTO;
import deltma.solutions.backend.models.MonthlyTimeRegister;
import deltma.solutions.backend.models.TimeRegister;
import deltma.solutions.backend.models.User;
import deltma.solutions.backend.repositories.MonthlyTimeRegisterRepository;
import deltma.solutions.backend.repositories.TimeRegisterRepository;
import deltma.solutions.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeRegisterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeRegisterRepository timeRegisterRepository;

    @Autowired
    private MonthlyTimeRegisterRepository monthlyTimeRegisterRepository;

    public TimeRegisterService(UserRepository userRepository, TimeRegisterRepository timeRegisterRepository, MonthlyTimeRegisterRepository monthlyTimeRegisterRepository) {
        this.userRepository = userRepository;
        this.timeRegisterRepository = timeRegisterRepository;
        this.monthlyTimeRegisterRepository = monthlyTimeRegisterRepository;
    }

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

        List<TimeRegister> timeRegistrations = timeRegisterRepository.findByUserEmailAndDateYearAndDateMonth(email, currentYear, currentMonth);

        return timeRegistrations.stream()
                .map(timeRegister -> new TimeRegisterRequestDTO(
                        timeRegister.getWorkHours(),
                        timeRegister.getDate(),
                        timeRegister.getUser().getEmail()
                ))
                .collect(Collectors.toList());
    }

    public List<TimeRegisterRequestDTO> getFormerRegistrationsForLastYear(String email) {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();

        // Calculate the starting month for the last 12 months
        int startMonth = currentMonth - 11;
        int startYear = currentYear;

        if (startMonth <= 0) {
            startMonth += 12;
            startYear--;
        }

        System.out.println("Start Month: " + startMonth);
        System.out.println("Start Year: " + startYear);

        // Use the repository method to get time registers for the last 12 months
        List<TimeRegister> timeRegistrations = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            int targetMonth = startMonth + i;
            int targetYear = startYear;

            if (targetMonth > 12) {
                targetMonth -= 12;
                targetYear++;
            }

            List<TimeRegister> registersForMonth =
                    timeRegisterRepository.findByUserEmailAndDateYearAndDateMonth(email, targetYear, targetMonth);

            if (!registersForMonth.isEmpty()) {
                timeRegistrations.addAll(registersForMonth);
            }
        }

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

        List<TimeRegister> timeRegistrations = timeRegisterRepository.findByUserEmailAndDateYearAndDateMonth(userEmail, currentYear, currentMonth);

        return timeRegistrations.stream()
                .mapToInt(timeRegister -> Integer.parseInt(timeRegister.getWorkHours()))
                .sum();
    }

    private int getTotalWorkHoursForUserAndMonth(String userEmail, int year, int month) {
        List<TimeRegister> timeRegistrations = timeRegisterRepository.findByUserEmailAndDateYearAndDateMonth(userEmail, year, month);

        return timeRegistrations.stream()
                .mapToInt(timeRegister -> Integer.parseInt(timeRegister.getWorkHours()))
                .sum();
    }

    @Transactional
    public void calculateAndStoreMonthlyTime() {
        try {
            System.out.println("Calculating and storing monthly time for all users...");

            // Get the current month and year
            int currentMonth = LocalDate.now().getMonthValue();
            int currentYear = LocalDate.now().getYear();

            // Iterate over users
            for (User user : userRepository.findAll()) {
                int totalWorkHoursForUser = getTotalWorkHoursForUserAndMonth(user.getEmail(), currentYear, currentMonth);

                // Check if a MonthlyTimeRegister already exists for the current user, year, and month
                List<MonthlyTimeRegister> existingRegisters =
                        monthlyTimeRegisterRepository.findByUserAndYearAndMonth(user, currentYear, currentMonth);

                if (existingRegisters.isEmpty()) {
                    // If not, insert a new MonthlyTimeRegister
                    MonthlyTimeRegister monthlyTimeRegister = new MonthlyTimeRegister(totalWorkHoursForUser, currentYear, currentMonth, user);
                    monthlyTimeRegisterRepository.save(monthlyTimeRegister);
                }
            }

            System.out.println("Monthly time calculation and storage completed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
