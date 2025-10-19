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

    @Pattern(regexp = "^\\d{10}$")
    @Size(max = 10)
    private String phoneNumber;

    @Size(max = 50)
    private String bankAccount;

    @Size(max = 100)
    private String bankName;

    @Pattern(regexp = "^\\d{13}$")
    @Size(max = 13)
    private String idCardNumber;

    @NotNull
    private Role userType;
}