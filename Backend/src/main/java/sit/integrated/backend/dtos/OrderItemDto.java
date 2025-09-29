package sit.integrated.backend.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sit.integrated.backend.entities.Order;
import sit.integrated.backend.entities.SaleItem;

@Data
public class OrderItemDto {
    private Integer no;
    private Long saleItemId;
    private Integer price;
    private Integer quantity;
    private String description;
    @JsonIgnore
    private Order order;
    @JsonIgnore
    private SaleItem saleItem;

    public Integer getNo() {
        return order != null ? order.getId() : null;
    }

    @JsonIgnore
    public Integer getSellerId() {
        return saleItem.getUser().getId();
    }
}
