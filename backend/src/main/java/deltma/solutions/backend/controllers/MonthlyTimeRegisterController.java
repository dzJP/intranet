package deltma.solutions.backend.controllers;

import deltma.solutions.backend.dto.MonthlyTimeDTO;
import deltma.solutions.backend.models.User;
import deltma.solutions.backend.services.MonthlyTimeRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MonthlyTimeRegisterController {

    @Autowired
    private MonthlyTimeRegisterService monthlyTimeRegisterService;

    @GetMapping("/totals-last-year")
    public ResponseEntity<List<MonthlyTimeDTO>> getMonthlyTotalsForLastYear(
            @RequestParam String userEmail, @RequestParam int month) {
        try {
            return ResponseEntity.ok(monthlyTimeRegisterService.getMonthlyTotalsForLastYear(userEmail, month));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in getMonthlyTotalsForLastYear: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

//    @PostMapping("/calculate-monthly-time")
//    public ResponseEntity<String> calculateMonthlyTime() {
//        try {
//            monthlyTimeRegisterService.saveAndResetMonthlyTime();
//            return ResponseEntity.ok("Monthly time calculation completed.");
//        } catch (Exception e) {
//
//            e.printStackTrace();
//            System.out.println("Error in calculateMonthlyTime: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//    }

    // For testing only
    @PostMapping("/save-and-reset-monthly-time")
    public ResponseEntity<Void> saveAndResetMonthlyTimeRegister() {
        try {
            monthlyTimeRegisterService.saveAndResetMonthlyTime();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}


