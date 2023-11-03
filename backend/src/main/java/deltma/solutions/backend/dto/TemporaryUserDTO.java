package deltma.solutions.backend.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class TemporaryUserDTO {

    private String email;
    private UUID uuid;

    public TemporaryUserDTO(String email, UUID uuid) {
        this.email = email;
        this.uuid = uuid;
    }

}

