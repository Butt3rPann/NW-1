package sit.integrated.backend.dtos;

import lombok.Data;

import java.time.Instant;

@Data
public class SellerResponseDto {
    private Integer id;
    private String nickname;
    private String email;
    private String fullname;
    private String mobileNumber;
    private String bankAccountNumber;
    private String bankName;
    private String nationalId;
    private SellerNationalIdPhotoDto sellerNationalIdPhotoDto;
    private Instant createdOn;
    private Instant updatedOn;
}
