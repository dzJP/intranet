package deltma.solutions.backend.services;

import deltma.solutions.backend.dto.MonthlyTimeDTO;
import deltma.solutions.backend.dto.TimeRegisterRequestDTO;
import deltma.solutions.backend.models.MonthlyTimeRegister;
import deltma.solutions.backend.models.User;
import deltma.solutions.backend.repositories.MonthlyTimeRegisterRepository;
import deltma.solutions.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MonthlyTimeRegisterService {

    @Autowired
    private TimeRegisterService timeRegisterService;
    @Autowired
    private MonthlyTimeRegisterRepository monthlyTimeRegisterRepository;
    @Autowired
    private UserRepository userRepository;

    private List<MonthlyTimeDTO> monthlyTimeList = new ArrayList<>();

    @Transactional
    public List<MonthlyTimeDTO> getTotalTimeForLastYear(String userEmail, int selectedMonth) {
        try {
            System.out.println("User Email: " + userEmail);

            User user = userRepository.findByEmail(userEmail);
            if (user == null) {
                // Handle the case where the user is not found
                return Collections.emptyList();
            }

            System.out.println("User roles: " + user.getRole());

            // Delegate the logic to calculate total time for the last year to another method
            List<MonthlyTimeDTO> totalTimeList = calculateTotalTimeForLastYear(user, selectedMonth);

            for (MonthlyTimeDTO monthlyTimeDTO : totalTimeList) {
                System.out.println("Total Time DTO: " + monthlyTimeDTO);
            }

            return totalTimeList;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void calculateMonthlyTimeForAllUsers() {
        try {
            System.out.println("Calculating monthly time for all users...");

            // Get all users
            List<User> users = userRepository.findAll();

            // Get the current month
            int currentMonth = LocalDate.now().getMonthValue();

            // Iterate over users and calculate monthly time for each
            for (User user : users) {
                System.out.println("Calculating monthly time for user: " + user.getEmail());

                // Use TimeRegisterService to get total work hours for the last 12 months
                List<TimeRegisterRequestDTO> timeRegistrations = timeRegisterService.getFormerRegistrationsForLastYear(user.getEmail());

                // Calculate the starting month for the last 12 months
                int startMonth = currentMonth - 11;
                int startYear = LocalDate.now().getYear();

                if (startMonth <= 0) {
                    startMonth += 12;
                    startYear--;
                }

                // Iterate over the last 12 months and save the totals
                for (int i = 0; i < 12; i++) {
                    int targetMonth = startMonth + i;
                    int targetYear = startYear;

                    if (targetMonth > 12) {
                        targetMonth -= 12;
                        targetYear++;
                    }

                    // Calculate total work hours for the last 12 months
                    int finalTargetMonth = targetMonth;
                    int finalTargetYear = targetYear;
                    int totalWorkHours = timeRegistrations.stream()
                            .filter(dto -> dto.getMonth() == finalTargetMonth && dto.getYear() == finalTargetYear)
                            .mapToInt(dto -> Integer.parseInt(dto.getWorkHours()))
                            .sum();

                    // Debugging statements
                    System.out.println("User: " + user.getEmail() + ", Month: " + targetMonth + ", Year: " + targetYear);
                    System.out.println("Total Work Hours: " + totalWorkHours);

                    // Save the calculated totals to MonthlyTimeRegister table
                    MonthlyTimeRegister monthlyTimeRegister = new MonthlyTimeRegister(
                            totalWorkHours,
                            targetYear,
                            targetMonth,
                            user
                    );
                    monthlyTimeRegisterRepository.save(monthlyTimeRegister);
                }
            }

            System.out.println("Monthly time calculation completed.");
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
        int currentYear = currentDate.getYear();
        List<MonthlyTimeRegister> monthlyTimeRegisters =
                monthlyTimeRegisterRepository.findByUserAndYearAndMonth(user, currentYear, selectedMonth);

        System.out.println("Querying for user " + user.getEmail() + " and year " + currentYear + " and month " + selectedMonth);

        return monthlyTimeRegisters;
    }

    private List<MonthlyTimeDTO> calculateTotalTimeForLastYear(User user, int selectedMonth) {
        List<MonthlyTimeDTO> monthlyTimeList = new ArrayList<>();

        // Use the service method to get total work hours for the last year
        List<MonthlyTimeRegister> monthlyTimeRegisters = getFormerMonths(user, selectedMonth);

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

    @Transactional
    public void resetMonthlyTimeForAllUsers(List<User> users) {
        for (User user : users) {
            System.out.println("Resetting monthly time for user: " + user.getEmail());
            monthlyTimeRegisterRepository.deleteByUser(user);
        }
    }
}

//    @Transactional
////    @Scheduled(cron = "0 0 1 1 * ?") // Execute at midnight on the first day of each month
//    @Scheduled(fixedRate = 50000) // Run every 50 seconds
//    public void insertMonthlyTimeForAllUsers() {
//        try {
//            System.out.println("Inserting monthly time for all users...");
//
//            // Get the current month and year
//            int currentMonth = LocalDate.now().getMonthValue();
//            int currentYear = LocalDate.now().getYear();
//
//            // Iterate over users
//            for (User user : userRepository.findAll()) {
//                // Check if a MonthlyTimeRegister already exists for the current user, year, and month
//                List<MonthlyTimeRegister> existingRegisters =
//                        monthlyTimeRegisterRepository.findByUserAndYearAndMonth(user, currentYear, currentMonth);
//
//                if (existingRegisters.isEmpty()) {
//                    // If not, insert a new MonthlyTimeRegister
//                    MonthlyTimeRegister monthlyTimeRegister = new MonthlyTimeRegister(0, currentYear, currentMonth, user);
//                    monthlyTimeRegisterRepository.save(monthlyTimeRegister);
//                }
//            }
//
//            System.out.println("Monthly time insedrtion completed.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

