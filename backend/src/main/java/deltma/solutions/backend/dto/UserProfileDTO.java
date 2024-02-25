package deltma.solutions.backend.dto;


import deltma.solutions.backend.models.Role;
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
    Role role;
    String profilePictureUrl;
}
