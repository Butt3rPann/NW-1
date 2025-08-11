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
        if (model == null || model.trim().isBlank())
            this.model = null;
        else
            this.model = model.trim();
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isBlank())
            this.description = null;
        else
            this.description = description.trim();
    }

    public void setColor(String color) {
    	if (color == null || color.isBlank()) {
		    this.color = null;
        } else {
		    this.color = color.trim();
	    }
    }
}
