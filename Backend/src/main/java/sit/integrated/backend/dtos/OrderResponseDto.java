package sit.integrated.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sit.integrated.backend.entities.User;

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
    private List<OrderItemDto> orderItems;
    private String orderStatus;
}
