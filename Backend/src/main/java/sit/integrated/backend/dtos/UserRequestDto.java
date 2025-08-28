package sit.integrated.backend.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import sit.integrated.backend.utils.Role;
import sit.integrated.backend.utils.UserStatus;

import java.util.List;

@Data
public class UserRequestDto {
    private Integer id;
    private String nickName;
    private String email;
    private String fullName;
    private String password;
    private String phoneNumber;
    private String bankAccount;
    private String bankName;
    private String idCardNumber;
    private Role userType;
    private MultipartFile idCardImageFront;
    private MultipartFile idCardImageBack;
    private UserStatus status;
}
