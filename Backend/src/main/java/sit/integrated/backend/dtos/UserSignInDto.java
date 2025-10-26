package sit.integrated.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignInDto {
    @Pattern(regexp = "^\\s*[^\\s@]+@[^\\s@]+\\.[^\\s@]+\\s*$")
    @Size(max = 50)
    @NotBlank
    private String email;

    @Size(max = 14)
    @NotBlank
    private String password;
}
