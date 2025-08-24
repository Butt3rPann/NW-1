package sit.integrated.backend.dtos;

import lombok.Data;
import sit.integrated.backend.utils.Role;
import sit.integrated.backend.utils.UserStatus;

import java.time.Instant;
import java.util.List;

@Data
public class UserResponseDto {
    private Integer id;
    private String nickname;
    private String email;
    private String fullname;
    private String mobileNumber;
    private String bankAccountNumber;
    private String bankName;
    private String nationalId;
    private List<String> sellerNationalIdPhotos;
    private Role userType;
    private UserStatus status;
    private Instant createdOn;
    private Instant updatedOn;
}
