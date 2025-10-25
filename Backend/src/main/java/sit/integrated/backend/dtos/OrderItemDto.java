package sit.integrated.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sit.integrated.backend.entities.Order;

@Data
public class OrderItemDto {
    private Integer no;

    @NotNull
    private Integer saleItemId;

    @NotNull
    private Integer price;

    @NotNull
    private Integer quantity;

    @NotNull
    private String description;

    private String image;

    @JsonIgnore
    private Order order;

    @JsonIgnore
    private Integer saleItemUserId;
}
