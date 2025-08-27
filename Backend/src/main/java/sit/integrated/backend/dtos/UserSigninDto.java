package sit.integrated.backend.dtos;

import lombok.Data;

@Data
public class UserSigninDto {
    private String email;
    private String password;
}
