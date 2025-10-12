package sit.integrated.backend.controllers.v1;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.integrated.backend.dtos.SaleItemDetailDto;
import sit.integrated.backend.dtos.SaleItemDto;
import sit.integrated.backend.dtos.SaleItemFormDto;
import sit.integrated.backend.entities.SaleItem;
import sit.integrated.backend.services.FileService;
import sit.integrated.backend.services.SaleItemService;
import java.util.List;
import sit.integrated.backend.utils.ListMapper;

@RestController
@RequestMapping("/v1")
public class SaleItemControllerV1 {
    @Autowired
    private SaleItemService saleItemService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    FileService fileService;

    @GetMapping("/sale-items")
    public ResponseEntity<List<SaleItemDto>> getSaleItem() {
        List<SaleItem> saleItems = saleItemService.getSaleItems();
        List<SaleItemDto> dtos = listMapper.mapList(saleItems, SaleItemDto.class, modelMapper);
        dtos.forEach(item -> item.setSaleItemImages(fileService.getSaleItemImages(item.getId())));
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/sale-items/{id}")
    public ResponseEntity<SaleItemDetailDto> getSaleItemDetail(@PathVariable Integer id) {
        SaleItem saleItem = saleItemService.getSaleItemDetail(id);
        return ResponseEntity.ok(modelMapper.map(saleItem, SaleItemDetailDto.class));
    }

    @PostMapping("/sale-items")
    public ResponseEntity<SaleItemDetailDto> createSaleItem(@Valid @RequestBody SaleItemFormDto formDto) {
        SaleItemDetailDto saleItem = saleItemService.createSaleItem(formDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saleItem);
    }

    @PutMapping("/sale-items/{id}")
    public ResponseEntity<SaleItemDetailDto> updateSaleItem(@PathVariable Integer id, @Valid @RequestBody SaleItemFormDto formDto) {
        SaleItemDetailDto updatedItem = saleItemService.updateSaleItem(id, formDto);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/sale-items/{id}")
    public ResponseEntity<Void> deleteSaleItem(@PathVariable Integer id) {
        saleItemService.deleteSaleItem(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
