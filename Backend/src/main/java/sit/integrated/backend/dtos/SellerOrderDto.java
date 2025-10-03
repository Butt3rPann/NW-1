package sit.integrated.backend.dtos;

import lombok.Data;

@Data
public class SellerOrderDto {
    private Integer id;
    private String email;
    private String fullName;
    private String userType;
    private String nickName;
}