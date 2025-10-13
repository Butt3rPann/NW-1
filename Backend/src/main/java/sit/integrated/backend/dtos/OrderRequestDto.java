package sit.integrated.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import sit.integrated.backend.utils.OrderStatus;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class OrderRequestDto {
    @NotNull
    private Integer buyerId;

    @NotNull
    private Integer sellerId;

    @NotNull
    @PastOrPresent
    private Instant orderDate;

    @NotBlank
    private String shippingAddress;

    private String orderNote;

    @NotEmpty
    private List<OrderItemDto> orderItems;

    private OrderStatus orderStatus;
}
