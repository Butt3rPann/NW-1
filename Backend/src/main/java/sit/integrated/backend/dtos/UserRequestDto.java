package sit.integrated.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import sit.integrated.backend.utils.Role;
import sit.integrated.backend.utils.UserStatus;

@Data
public class UserRequestDto {
    private Integer id;

    @NotBlank
    private String nickName;

    @Pattern(regexp = "^\\s*[^\\s@]+@[^\\s@]+\\.[^\\s@]+\\s*$")
    @Size(max = 50)
    @NotBlank
    private String email;

    @Size(max = 40, min = 4)
    @NotBlank
    private String fullName;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).+$")
    @Size(max = 14, min = 8)
    @NotBlank
    private String password;

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

    private MultipartFile idCardImageFront;

    private MultipartFile idCardImageBack;

    private UserStatus status;
}
