package sit.integrated.backend.dtos;

import lombok.Data;

@Data
public class UserSignInDto {
    private String email;
    private String password;
}
