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

    public void calculateMonthlyTimeForAllUsers() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        List<User> users = userRepository.findAll();

        for (User user : users) {
            List<TimeRegister> timeRegistrations = timeRegisterRepository.findByUserEmailAndDateYearAndDateMonth(
                    user.getEmail(), currentYear, currentMonth);

            int totalTimeForUser = timeRegistrations.stream()
                    .mapToInt(TimeRegister::getWorkHours)
                    .sum();

            MonthlyTimeRegister monthlyTimeRegister = monthlyTimeRegisterRepository
                    .findByUserAndYearAndMonth(user, currentYear, currentMonth);

            if (monthlyTimeRegister == null) {
                monthlyTimeRegister = new MonthlyTimeRegister(0, currentYear, currentMonth, user);
            }

            monthlyTimeRegister.setTotalTime(totalTimeForUser);
            monthlyTimeRegisterRepository.save(monthlyTimeRegister);
        }
    }

    private List<MonthlyTimeRegister> getFormerMonths(User user, int selectedMonth) {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        List<MonthlyTimeRegister> monthlyTimeRegisters =
                monthlyTimeRegisterRepository.findAllByUserAndYearAndMonth(user, currentYear, selectedMonth);

        System.out.println("Querying for user " + user.getEmail() +
                " and year " + currentYear + " and month " + selectedMonth);

        return monthlyTimeRegisters;
    }

    public List<MonthlyTimeDTO> calculateTotalTimeForLastYear(String email, int selectedMonth) {
        List<MonthlyTimeDTO> monthlyTimeList = new ArrayList<>();
        User user = userRepository.findByEmail(email);

        List<MonthlyTimeRegister> monthlyTimeRegisters = getFormerMonths(user, selectedMonth);

        for (MonthlyTimeRegister monthlyTimeRegister : monthlyTimeRegisters) {

            if (monthlyTimeRegister.getMonth() <= selectedMonth) {
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

    public void resetMonthlyTimeForAllUsers(int currentYear, int currentMonth) {
        int nextMonth = currentMonth % 12 + 1;
        int nextYear = currentMonth == 12 ? currentYear + 1 : currentYear;

        List<User> users = userRepository.findAll();

        for (User user : users) {
            MonthlyTimeRegister nextMonthRecord = monthlyTimeRegisterRepository
                    .findByUserAndYearAndMonth(user, nextYear, nextMonth);

            if (nextMonthRecord != null) {
                nextMonthRecord.setTotalTime(0);
            } else {
                nextMonthRecord = new MonthlyTimeRegister(0, nextYear, nextMonth, user);
            }

            monthlyTimeRegisterRepository.save(nextMonthRecord);
        }
    }

    @Scheduled(cron = "0 0 0 L * ?")
    public void saveAndResetMonthlyTime() {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();

        calculateMonthlyTimeForAllUsers();
        resetMonthlyTimeForAllUsers(currentYear, currentMonth);
    }

}

