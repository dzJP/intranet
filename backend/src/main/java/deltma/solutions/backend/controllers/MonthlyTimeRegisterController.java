package deltma.solutions.backend.controllers;

import deltma.solutions.backend.dto.MonthlyTimeDTO;
import deltma.solutions.backend.models.User;
import deltma.solutions.backend.repositories.UserRepository;
import deltma.solutions.backend.services.MonthlyTimeRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MonthlyTimeRegisterController {

    @Autowired
    private MonthlyTimeRegisterService monthlyTimeRegisterService;
    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
    @GetMapping("/totals-last-year")
    public ResponseEntity<List<MonthlyTimeDTO>> getMonthlyTotalsForLastYear(@RequestParam int month) {
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

}
