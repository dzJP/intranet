package deltma.solutions.backend.controllers;

import deltma.solutions.backend.dto.MonthlyTimeDTO;
import deltma.solutions.backend.dto.TimeRegisterRequestDTO;
import deltma.solutions.backend.models.MonthlyTimeRegister;
import deltma.solutions.backend.models.User;
import deltma.solutions.backend.repositories.MonthlyTimeRegisterRepository;
import deltma.solutions.backend.repositories.UserRepository;
import deltma.solutions.backend.services.MonthlyTimeRegisterService;
import deltma.solutions.backend.services.TimeRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MonthlyTimeRegisterController {

    @Autowired
    private TimeRegisterService timeRegisterService;
    @Autowired
    private MonthlyTimeRegisterService monthlyTimeRegisterService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MonthlyTimeRegisterRepository monthlyTimeRegisterRepository;

    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
    @GetMapping("/totals-last-year")
    public ResponseEntity<List<MonthlyTimeDTO>> getMonthlyTotalsForLastYear(@RequestParam(name = "month", defaultValue = "0") int month) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            System.out.println("Received month parameter: " + month);
            System.out.println("User Email: " + userDetails.getUsername());

            User user = userRepository.findByEmail(userDetails.getUsername());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            System.out.println("User roles: " + user.getRole());

            List<MonthlyTimeDTO> monthlyTimeList = monthlyTimeRegisterService.getTotalTimeForLastYear(user, month);

            System.out.println("Returning data: " + monthlyTimeList);
            return ResponseEntity.ok(monthlyTimeList);
        } catch (Exception e) {
            // Log the exception details for debugging
            e.printStackTrace();
            System.out.println("Error in getMonthlyTotalsForLastYear: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/calculate-monthly-time")
    public ResponseEntity<Void> calculateMonthlyTime() {
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
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Log the exception details for debugging
            e.printStackTrace();
            System.out.println("Error in calculateMonthlyTime: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reset-monthly-time")
    public ResponseEntity<Void> resetMonthlyTime() {
        try {
            System.out.println("Resetting monthly time for all users...");

            // Get all users
            List<User> users = userRepository.findAll();

            // Iterate over users and reset monthly time for each
            for (User user : users) {
                System.out.println("Resetting monthly time for user: " + user.getEmail());

                // Reset monthly time for the user
                monthlyTimeRegisterRepository.deleteByUser(user);
            }

            System.out.println("Monthly time reset completed.");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Log the exception details for debugging
            e.printStackTrace();
            System.out.println("Error in resetMonthlyTime: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


