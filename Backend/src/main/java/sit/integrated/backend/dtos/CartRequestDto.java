package sit.integrated.backend.dtos;

import lombok.Data;

@Data
public class CartRequestDto {
    private Integer userId;
    private Integer saleItemId;
    private String model;
    private String brandName;
    private String color;
    private Integer storageGb;
    private Integer quantity;
    private Integer maxQuantity;
    private Integer priceEach;
}
