package sit.integrated.backend.dtos;

import lombok.Data;
import sit.integrated.backend.utils.Role;

@Data
public class ProfileDto {
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