package sit.integrated.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserDto {
    private Integer id;

    @JsonIgnore
    private String nickName;

    public String getUserName() {
        return nickName;
    }
}
