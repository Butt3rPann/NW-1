package sit.integrated.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sit.integrated.backend.entities.User;

import java.time.Instant;
import java.util.List;

@Data
public class SellerOrdersResponseDto {
    private Integer id;
    @JsonIgnore
    private UserDto user;
    private Integer sellerId;
    private Instant orderDate;
    @JsonIgnore
    private Instant createdOn;
    private String shippingAddress;
    private String orderNote;
    private List<OrderItemDto> orderItems;
    private String orderStatus;

    public Instant getPaymentDate() { return createdOn; }
    public UserDto getBuyer() { return user;}
}
