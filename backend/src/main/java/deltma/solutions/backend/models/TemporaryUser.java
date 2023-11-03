package deltma.solutions.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@Entity(name = "temporary_users")
public class TemporaryUser {

    @Id
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    private UUID uuid;

    public TemporaryUser(String email) {
        this.email = email;
        this.uuid = UUID.randomUUID();
    }

}

