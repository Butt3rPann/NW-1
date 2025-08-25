package sit.integrated.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sit.integrated.backend.utils.Role;
import sit.integrated.backend.utils.UserStatus;

import java.time.Instant;
import java.util.List;

@Data
public class UserResponseDto {
    private Integer id;
    private String nickName;
    private String email;
    private String fullName;
    private String phoneNumber;
    @JsonIgnore
    private UserStatus status;
    public boolean getIsActive() {
        return status.equals(UserStatus.ACTIVE);
    }
    private Role userType;
}
