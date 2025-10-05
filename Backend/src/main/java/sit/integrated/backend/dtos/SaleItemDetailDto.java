package sit.integrated.backend.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class SaleItemDetailDto {
    private Integer id;
    private String model;
    private String brandName;
    private String description;
    private Integer price;
    private Integer ramGb;
    private BigDecimal screenSizeInch;
    private Integer quantity;
    private Integer storageGb;
    private String color;
    private UserDto seller;
    private List<SaleItemImageDto> saleItemImages;
    private Instant createdOn;
    private Instant updatedOn;
}


