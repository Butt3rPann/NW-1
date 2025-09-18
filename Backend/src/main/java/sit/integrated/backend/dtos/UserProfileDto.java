package sit.integrated.backend.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import sit.integrated.backend.utils.Role;
import sit.integrated.backend.utils.UserStatus;

@Data
public class UserProfileDto {
    private Integer id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String bankAccount;
    private String bankName;
    private String idCardNumber;
    private Role userType;
    private String nickName;
}