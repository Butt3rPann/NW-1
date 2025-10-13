package sit.integrated.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;
import sit.integrated.backend.utils.Role;

@Data
public class UserProfileDto {
    @Pattern(regexp = "^\\s*[^\\s@]+@[^\\s@]+\\.[^\\s@]+\\s*$")
    @Size(max = 50)
    @NotBlank
    private String email;

    @NotBlank
    private String nickName;

    @Size(max = 40, min = 4)
    @NotBlank
    private String fullName;

    @Size(max = 20)
    private String phoneNumber;

    @Size(max = 50)
    private String bankAccount;

    @Size(max = 100)
    private String bankName;

    @Size(max = 25)
    private String idCardNumber;

    @NotNull
    private Role userType;
}