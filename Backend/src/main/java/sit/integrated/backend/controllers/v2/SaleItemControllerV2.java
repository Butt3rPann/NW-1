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
import sit.integrated.backend.utils.ListMapper;

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

    @GetMapping("/sale-items")
    public ResponseEntity<PageDto<SaleItemDetailDto>> getSaleItems(
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @RequestParam(required = false, defaultValue = "") List<String> filterBrands
            ) {
        Page<SaleItem> page = saleItemService.getSaleItems(filterBrands,sortField, sortDirection);
        return ResponseEntity.ok(listMapper.toPageDto(page, SaleItemDetailDto.class, modelMapper));
    }
}
