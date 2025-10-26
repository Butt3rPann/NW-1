package sit.integrated.backend.dtos;

import lombok.Data;

@Data
public class SaleItemListDto {
    private Integer id;
    private String model;
    private String brandName;
    private Integer price;
    private Integer storageGb;
    private Integer ramGb;
    private String color;
    private UserDto seller;
}

