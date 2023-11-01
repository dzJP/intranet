package deltma.solutions.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class TemporaryUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String email;

    @Column(unique = true)
    UUID uuid;

    public TemporaryUser(String email) {
        this.email = email;
        this.uuid = UUID.randomUUID();
    }

}
