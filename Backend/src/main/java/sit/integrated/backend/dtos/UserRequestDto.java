package sit.integrated.backend.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import sit.integrated.backend.utils.Role;
import sit.integrated.backend.utils.UserStatus;

import java.util.List;

@Data
public class UserRequestDto {
    private Integer id;
    private String nickname;
    private String email;
    private String password;
    private String fullname;
    private String mobileNumber;
    private String bankAccountNumber;
    private String bankName;
    private String nationalId;
    private List<MultipartFile> sellerNationalIdPhotos;
    private Role userType;
    private UserStatus status;
}
