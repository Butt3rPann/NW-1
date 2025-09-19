package sit.integrated.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sit.integrated.backend.utils.Role;
import sit.integrated.backend.utils.UserStatus;

@Data
public class BuyerResponseDto {
    private Integer id;
    private String email;
    private String fullName;
    private Role userType;
    private String nickName;
}
