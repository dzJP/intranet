package deltma.solutions.backend.services;

import deltma.solutions.backend.dto.TimeRegisterRequestDTO;
import deltma.solutions.backend.models.TimeRegister;
import deltma.solutions.backend.models.User;
import deltma.solutions.backend.repositories.TimeRegisterRepository;
import deltma.solutions.backend.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeRegisterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeRegisterRepository timeRegisterRepository;

    public void registerTime(TimeRegisterRequestDTO timeRegisterRequestDTO) {
        // Retrieve the user based on the email
        User user = userRepository.findByEmail(timeRegisterRequestDTO.getEmail());

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Create a TimeRegister entity from the DTO
        TimeRegister timeRegister = new TimeRegister();
        timeRegister.setHoursWorked(timeRegisterRequestDTO.getHoursWorked());
        timeRegister.setDate(timeRegisterRequestDTO.getDate());
        timeRegister.setUser(user);

        // Save the TimeRegister entity
        timeRegisterRepository.save(timeRegister);
    }
}





