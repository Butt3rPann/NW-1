package sit.integrated.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sit.integrated.backend.entities.User;
import java.time.Instant;
import java.util.List;

@Data
public class OrderDto {
    private Integer id;
    private Integer buyerId;
    private SellerOrderDto seller;
    private Instant orderDate;
    private String shippingAddress;
    private String orderNote;
    private List<OrderItemDto> orderItems;
    private String orderStatus;
    @JsonIgnore
    private User user;
    @JsonIgnore
    private Instant createdOn;

    public Instant getPaymentDate() { return createdOn; }
}
