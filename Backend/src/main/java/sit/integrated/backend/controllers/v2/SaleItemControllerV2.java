package sit.integrated.backend.controllers.v2;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.integrated.backend.dtos.PageDto;
import sit.integrated.backend.dtos.SaleItemDetailDto;
import sit.integrated.backend.entities.SaleItem;
import sit.integrated.backend.services.SaleItemService;
import sit.integrated.backend.services.StorageSizeService;
import sit.integrated.backend.utils.ListMapper;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/v2")
public class SaleItemControllerV2 {
    @Autowired
    private SaleItemService saleItemService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private StorageSizeService storageSizeService;

    @GetMapping("/sale-items")
    public ResponseEntity<PageDto<SaleItemDetailDto>> getSaleItems(@RequestParam(required = false) String sortField,
                                                                   @RequestParam(required = false, defaultValue = "asc") String sortDirection,
                                                                   @RequestParam(required = false) List<String> filterBrands,
                                                                   @RequestParam(required = false) List<Integer> filterStorages,
                                                                   @RequestParam(required = false) Integer filterPriceLower,
                                                                   @RequestParam(required = false) Integer filterPriceUpper,
                                                                   @RequestParam Integer page,
                                                                   @RequestParam(required = false, defaultValue = "10") Integer size) {

        if(filterPriceLower != null && filterPriceUpper != null && filterPriceLower > filterPriceUpper) {
            int temp = filterPriceLower;
            filterPriceLower = filterPriceUpper;
            filterPriceUpper = temp;
        }

        Page<SaleItem> saleItems = saleItemService.getSaleItems(filterBrands, filterStorages, filterPriceLower, filterPriceUpper, sortField, sortDirection, page, size);
        return ResponseEntity.ok(listMapper.toPageDto(saleItems, SaleItemDetailDto.class, modelMapper));
    }
}