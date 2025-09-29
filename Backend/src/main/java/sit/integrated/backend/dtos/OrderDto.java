package sit.integrated.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sit.integrated.backend.entities.User;
import java.time.Instant;
import java.util.List;

@Data
public class OrderDto {
    private Integer buyerId;
    private Integer sellerId;
    private Instant orderDate;
    private String shippingAddress;
    private String orderNote;
    private List<OrderItemDto> orderItems;
    private String orderStatus;
    @JsonIgnore
    private User user;

    public Integer getBuyerId() {
        return user != null ? user.getId() : null;
    }

    public Integer getSellerId() {
        return orderItems.stream().findFirst().map(OrderItemDto::getSellerId).orElse(null);
    }

}
