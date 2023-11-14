package deltma.solutions.backend.dto;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    String email;
    String firstName;
    String lastName;
    String phoneNumber;
}
