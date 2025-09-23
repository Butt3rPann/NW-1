package sit.integrated.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class SellerDto {
    private Integer id;
    @JsonIgnore
    private String nickName;

    public String getUserName() {
        return nickName;
    }
}
