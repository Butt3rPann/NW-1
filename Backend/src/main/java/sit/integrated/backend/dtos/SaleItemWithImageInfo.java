package sit.integrated.backend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class SaleItemWithImageInfo {
    private SaleItemFormDto saleItem;
    private List<SaleItemImageRequest> imageInfos;
}
