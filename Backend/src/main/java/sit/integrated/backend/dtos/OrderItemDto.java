package sit.integrated.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sit.integrated.backend.entities.Order;
import sit.integrated.backend.entities.SaleItem;

@Data
public class OrderItemDto {
    private Integer no;
    private Integer saleItemId;
    private Integer price;
    private Integer quantity;
    private String description;
    @JsonIgnore
    private Order order;
    @JsonIgnore
    private Integer saleItemUserId;
}
