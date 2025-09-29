package sit.integrated.backend.dtos;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class OrderResponseDto {
    private Integer id;
    private Integer buyerId;
    private SellerOrderDto seller;
    private Instant orderDate;
    private String shippingAddress;
    private String orderNote;
    private Set<OrderItemDto> orderItems;
    private String orderStatus;
}
