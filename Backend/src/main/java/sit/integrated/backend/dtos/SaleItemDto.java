package sit.integrated.backend.dtos;

import lombok.Data;

@Data
public class SaleItemDto {
    private Integer id;
    private String model;
    private String brandName;
    private Integer price;
    private Integer ramGb;
    private Integer storageGb;
    private String color;
}
