package sit.integrated.backend.dtos;

import lombok.Data;

import java.time.Instant;

@Data
public class BuyerResponseDto {
    private Integer id;
    private String nickname;
    private String email;
    private String fullname;
    private Instant createdOn;
    private Instant updatedOn;
}
