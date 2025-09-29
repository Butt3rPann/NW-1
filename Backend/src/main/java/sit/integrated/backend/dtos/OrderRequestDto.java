package sit.integrated.backend.dtos;

import lombok.Data;
import sit.integrated.backend.utils.OrderStatus;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
public class OrderRequestDto {
    private Integer id;
    private Integer buyerId;
    private Integer sellerId;
    private Instant orderDate;
    private String shippingAddress;
    private String orderNote;
    private Set<OrderItemDto> orderItems;
    private OrderStatus orderStatus;

    private void setOrderStatus() {
        this.orderStatus = OrderStatus.COMPLETED;
    }
}
