package sit.integrated.backend.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SellerResponseDto extends BuyerResponseDto {
    private String phoneNumber;
    private String bankName;
    private String bankAccount;
}
