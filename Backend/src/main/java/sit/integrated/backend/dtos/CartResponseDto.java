package sit.integrated.backend.dtos;

import lombok.Data;

@Data
public class CartResponseDto {
    private Integer id;
    private SellerDto seller;
    private Integer saleItemId;
    private String model;
    private String brandName;
    private String color;
    private Integer storageGb;
    private Integer quantity;
    private Integer maxQuantity;
    private Integer priceEach;
}
