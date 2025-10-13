package sit.integrated.backend.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartRequestDto {
    @NotNull
    private Integer userId;

    @NotNull
    private Integer saleItemId;

    @NotBlank
    private String model;

    @NotBlank
    private String brandName;

    private String color;

    private Integer storageGb;

    @Min(1)
    @NotNull
    private Integer quantity;

    @NotNull
    private Integer maxQuantity;

    @NotNull
    private Integer priceEach;
}
