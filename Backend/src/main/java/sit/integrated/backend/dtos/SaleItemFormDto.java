package sit.integrated.backend.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleItemFormDto {
    private Integer id;
    private String model;
    private BrandDto brand;
    private String description;
    private Integer price;
    private Integer ramGb;
    private BigDecimal screenSizeInch;
    private Integer storageGb;
    private String color;
    private Integer quantity;

    public void setModel(String model) {
        this.model = model.trim();
    }
    public void setDescription(String description) {
        this.description = description.trim();
    }
    public void setColor(String color) {
        this.color = color.trim();
    }
    public void setQuantity(Integer quantity) {
        if (quantity == null || quantity < 0) {
            this.quantity = 1;
        } else {
            this.quantity = quantity;
        }
    }
}
