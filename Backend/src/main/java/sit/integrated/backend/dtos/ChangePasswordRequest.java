package sit.integrated.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    private Integer id;
    @Size(max = 14)
    @NotBlank
    private String oldPassword;
    @Size(max = 14)
    @NotBlank
    private String newPassword;
    @Size(max = 14)
    @NotBlank
    private String confirmPassword;
}
