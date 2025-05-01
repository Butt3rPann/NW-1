package sit.integrated.backend.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.integrated.backend.dtos.SaleItemDto;
import sit.integrated.backend.services.SaleItemService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/itb-mshop/v1")
public class SaleItemController {
    @Autowired
    private SaleItemService saleItemService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/sale-items")
    public ResponseEntity<List<SaleItemDto>> getSaleItem() {
        return ResponseEntity.ok().body(
                saleItemService.getSaleItems()
                .stream()
                .map(item -> {
                    SaleItemDto dto = modelMapper.map(item, SaleItemDto.class);
                    dto.setBrandName(item.getBrand().getName());
                    return dto;
                })
                .collect(Collectors.toList()));
    }
}
