package sit.integrated.backend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CartItemDto {
    private Integer id;
    private Integer saleItemId;
    private String model;
    private String brandName;
    private String color;
    private Integer storageGb;
    private Integer quantity;
    private Integer maxQuantity;
    private Integer priceEach;
    private List<String> saleItemImg;
}
