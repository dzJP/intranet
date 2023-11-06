package deltma.solutions.backend.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class TemporaryUserDTO {

    private String email;
    private String uuid;

    public TemporaryUserDTO(String email, String uuid) {
        this.email = email;
        this.uuid = uuid;
    }

}

