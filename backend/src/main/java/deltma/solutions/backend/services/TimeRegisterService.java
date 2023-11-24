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
}





