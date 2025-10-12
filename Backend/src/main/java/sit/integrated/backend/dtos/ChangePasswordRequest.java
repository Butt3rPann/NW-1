package sit.integrated.backend.dtos;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private Integer id;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
