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

            List<MonthlyTimeDTO> monthlyTimeList = monthlyTimeRegisterService.getTotalTimeForLastYear(userDetails.getUsername(), month);

            System.out.println("Returning data: " + monthlyTimeList);
            return ResponseEntity.ok(monthlyTimeList);
        } catch (Exception e) {

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

            // Call the service method to calculate monthly time
            monthlyTimeRegisterService.calculateMonthlyTimeForAllUsers();

            System.out.println("Monthly time calculation completed.");
            return ResponseEntity.ok().build();
        } catch (Exception e) {

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

            // Call the service method to reset monthly time
            monthlyTimeRegisterService.resetMonthlyTimeForAllUsers(users);

            System.out.println("Monthly time reset completed.");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in resetMonthlyTime: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


