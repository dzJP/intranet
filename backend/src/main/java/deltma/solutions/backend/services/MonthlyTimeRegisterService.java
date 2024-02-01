package deltma.solutions.backend.services;

import deltma.solutions.backend.dto.MonthlyTimeDTO;
import deltma.solutions.backend.models.MonthlyTimeRegister;
import deltma.solutions.backend.models.TimeRegister;
import deltma.solutions.backend.models.User;
import deltma.solutions.backend.repositories.MonthlyTimeRegisterRepository;
import deltma.solutions.backend.repositories.TimeRegisterRepository;
import deltma.solutions.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableScheduling
public class MonthlyTimeRegisterService {

    @Autowired
    private MonthlyTimeRegisterRepository monthlyTimeRegisterRepository;
    @Autowired
    private TimeRegisterRepository timeRegisterRepository;
    @Autowired
    private UserRepository userRepository;

    public List<MonthlyTimeDTO> getMonthlyTotalsForLastYear(String email, int selectedMonth) {
        List<MonthlyTimeDTO> monthlyTimeList = new ArrayList<>();
        User user = userRepository.findByEmail(email);

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        for (int year = currentYear - 1; year <= currentYear; year++) {
            List<MonthlyTimeRegister> monthlyTimeRegisters =
                    monthlyTimeRegisterRepository.findAllByUserAndYearAndMonth(user, year, selectedMonth);

            for (MonthlyTimeRegister monthlyTimeRegister : monthlyTimeRegisters) {
                if (year == currentYear && monthlyTimeRegister.getMonth() > selectedMonth) {
                    continue;
                }

                int totalTimeForMonth = monthlyTimeRegister.getTotalTime();
                MonthlyTimeDTO monthlyTimeDTO = new MonthlyTimeDTO(
                        monthlyTimeRegister.getYear(),
                        monthlyTimeRegister.getMonth(),
                        totalTimeForMonth
                );
                monthlyTimeList.add(monthlyTimeDTO);
            }
        }
        return monthlyTimeList;
    }

    private void saveMonthlyTimeRegisters(int year, int month) {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            List<TimeRegister> timeRegistrations = timeRegisterRepository.findByUserEmailAndDateYearAndDateMonth(
                    user.getEmail(), year, month);

            int totalTimeForUser = timeRegistrations.stream()
                    .mapToInt(TimeRegister::getWorkHours)
                    .sum();

            MonthlyTimeRegister monthlyTimeRegister = monthlyTimeRegisterRepository
                    .findByUserAndYearAndMonth(user, year, month);

            if (monthlyTimeRegister == null) {
                monthlyTimeRegister = new MonthlyTimeRegister(0, year, month, user);
            }

            monthlyTimeRegister.setTotalTime(totalTimeForUser);
            monthlyTimeRegisterRepository.save(monthlyTimeRegister);
        }
    }

    @Scheduled(cron = "0 0 0 10 * ?")
    public void scheduleSaveMonthlyTimeRegisters() {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastMonth = currentDate.minusMonths(1);
        int lastMonthYear = lastMonth.getYear();
        int lastMonthValue = lastMonth.getMonthValue();
        saveMonthlyTimeRegisters(lastMonthYear, lastMonthValue);
    }

}

