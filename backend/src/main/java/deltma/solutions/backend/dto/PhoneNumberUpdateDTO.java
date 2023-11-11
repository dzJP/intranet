package deltma.solutions.backend.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PhoneNumberUpdateDTO {
    String email;
    String phoneNumber;
}
