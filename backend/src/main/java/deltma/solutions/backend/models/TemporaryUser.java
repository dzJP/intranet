package deltma.solutions.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Entity(name = "temporary_users")
public class TemporaryUser {

    @Id
    @Column(name = "email", nullable = false, unique = true)
    String email;

    UUID uuid;

    public TemporaryUser(String email) {
        this.email = email;
        this.uuid = UUID.randomUUID();
    }
}
