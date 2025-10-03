package sit.integrated.backend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CartSellerWithItemsDto {
    private SellerDto seller;
    private List<CartItemDto> cartItems;
}
