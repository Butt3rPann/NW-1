package sit.integrated.backend.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleItemFormDto {
    private Integer id;

    @Size(max = 60)
    @NotBlank
    private String model;

    @NotNull
    private BrandDto brand;

    @Size(max = 16384)
    @NotBlank
    private String description;

    @NotNull
    @Min(0)
    private Integer price;

    private Integer ramGb;

    @Digits(integer = 2, fraction = 2)
    @DecimalMin(value = "1.00", inclusive = true)
    private BigDecimal screenSizeInch;

    private Integer storageGb;

    @Size(max = 40)
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
