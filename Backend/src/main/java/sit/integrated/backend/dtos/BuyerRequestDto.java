package sit.integrated.backend.dtos;

import lombok.Data;

@Data
public class BuyerRequestDto {
    private Integer id;
    private String nickname;
    private String email;
    private String password;
    private String fullname;
}
