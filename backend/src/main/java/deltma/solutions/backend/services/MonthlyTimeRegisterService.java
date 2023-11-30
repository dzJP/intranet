package deltma.solutions.backend.services;

import deltma.solutions.backend.dto.MonthlyTimeDTO;
import deltma.solutions.backend.models.MonthlyTimeRegister;
import deltma.solutions.backend.models.User;
import deltma.solutions.backend.repositories.MonthlyTimeRegisterRepository;
import deltma.solutions.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MonthlyTimeRegisterService {

    @Autowired
    private MonthlyTimeRegisterRepository monthlyTimeRegisterRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<MonthlyTimeDTO> getTotalTimeForLastYear(User user, int selectedMonth) {
        try {
            System.out.println("User Email: " + user.getEmail());
            System.out.println("Selected Month: " + selectedMonth);

            // Use the repository method to get monthly time registers
            List<MonthlyTimeRegister> monthlyTimeRegisters =
                    monthlyTimeRegisterRepository.findByUserAndYearAndMonthBefore(user, LocalDate.now().getYear(), selectedMonth);

            System.out.println("Found monthly time registers: " + monthlyTimeRegisters.size());

            if (!monthlyTimeRegisters.isEmpty()) {
                for (MonthlyTimeRegister monthlyTimeRegister : monthlyTimeRegisters) {
                    System.out.println("Monthly Time Register: " + monthlyTimeRegister);
                }
            } else {
                System.out.println("No monthly time registers found for the selected month (" + selectedMonth + ")");
            }

            List<MonthlyTimeDTO> totalTimeList = calculateTotalTimeForLastYear(monthlyTimeRegisters, selectedMonth);

            for (MonthlyTimeDTO monthlyTimeDTO : totalTimeList) {
                System.out.println("Total Time DTO: " + monthlyTimeDTO);
            }

            return totalTimeList;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    @Scheduled(fixedRate = 60000) // Run every 60 seconds
//    @Scheduled(cron = "0 0 1 1 * ?") // Execute at midnight on the first day of each month
    public void calculateMonthlyTime() {
        try {
            System.out.println("Calculating monthly time for all users...");
            // Get all users (you may need to adjust this based on your data structure)
            List<User> users = userRepository.findAll();
            // Get the current month
            int currentMonth = LocalDate.now().getMonthValue();
            // Iterate over users and calculate monthly time for each
            for (User user : users) {
                List<MonthlyTimeRegister> monthlyTimeRegisters = getFormerMonths(user, currentMonth);
                System.out.println("Found monthly time registers: " + monthlyTimeRegisters.size());
                // Calculate total time for last year for the user
                List<MonthlyTimeDTO> monthlyTimeList = calculateTotalTimeForLastYear(monthlyTimeRegisters, currentMonth);
                // Save the calculated totals to the MonthlyTimeRegister table
                saveMonthlyTimeTotals(user, monthlyTimeList);
            }
            System.out.println("Monthly time calculation completed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
//    @Scheduled(cron = "0 0 1 1 * ?") // Execute at midnight on the first day of each month
    @Scheduled(fixedRate = 50000) // Run every 50 seconds
    public void insertMonthlyTimeForAllUsers() {
        try {
            System.out.println("Inserting monthly time for all users...");

            // Get all users
            List<User> users = userRepository.findAll();

            // Get the current month and year
            int currentMonth = LocalDate.now().getMonthValue();
            int currentYear = LocalDate.now().getYear();

            // Iterate over users and insert monthly time
            for (User user : users) {
                MonthlyTimeRegister monthlyTimeRegister = new MonthlyTimeRegister(0, currentYear, currentMonth, user);
                monthlyTimeRegisterRepository.save(monthlyTimeRegister);
            }

            System.out.println("Monthly time insertion completed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void saveMonthlyTimeTotals(User user, List<MonthlyTimeDTO> monthlyTimeList) {
        System.out.println("Saving monthly time totals for user: " + user.getEmail());
        for (MonthlyTimeDTO monthlyTimeDTO : monthlyTimeList) {
            System.out.println("DTO: " + monthlyTimeDTO);
            MonthlyTimeRegister monthlyTimeRegister = new MonthlyTimeRegister(
                    monthlyTimeDTO.getTotalTime(),
                    monthlyTimeDTO.getYear(),
                    monthlyTimeDTO.getMonth(),
                    user
            );
            monthlyTimeRegisterRepository.save(monthlyTimeRegister);
        }
    }

    private List<MonthlyTimeRegister> getFormerMonths(User user, int selectedMonth) {
        LocalDate currentDate = LocalDate.now();
        List<MonthlyTimeRegister> monthlyTimeRegisters =
                monthlyTimeRegisterRepository.findByUserAndYearAndMonthBefore(user, currentDate.getYear(), selectedMonth);

        System.out.println("Querying for user " + user.getEmail() + " and year " + currentDate.getYear() + " and month " + selectedMonth);

        return monthlyTimeRegisters;
    }


    private List<MonthlyTimeDTO> calculateTotalTimeForLastYear(List<MonthlyTimeRegister> monthlyTimeRegisters, int selectedMonth) {
        List<MonthlyTimeDTO> monthlyTimeList = new ArrayList<>();
        for (MonthlyTimeRegister monthlyTimeRegister : monthlyTimeRegisters) {
            // Only include entries up to the selected month
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
}
