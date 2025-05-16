package sit.integrated.backend.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.integrated.backend.dtos.BrandDetailDto;
import sit.integrated.backend.dtos.BrandDto;
import sit.integrated.backend.dtos.BrandFormDto;
import sit.integrated.backend.entities.Brand;
import sit.integrated.backend.services.BrandService;
import sit.integrated.backend.utils.ListMapper;
import java.util.List;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "http://localhost:5173")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/brands")
    public ResponseEntity<List<BrandDto>> getAllBrands() {
        List<Brand> brands = brandService.getAllBrands("id", "ASC");
        return ResponseEntity.ok(listMapper.mapList(brands,BrandDto.class, modelMapper));
    }

    @GetMapping("/brands/{id}")
    public ResponseEntity<BrandDetailDto> getBrand(@PathVariable Integer id) {
        Brand brand = brandService.getBrandById(id);
        BrandDetailDto brandDetailDto = modelMapper.map(brand, BrandDetailDto.class);
        brandDetailDto.setNoOfSaleItem(brand.getSaleItems().size());
        return ResponseEntity.ok(brandDetailDto);
    }

    @PostMapping("/brands")
    public ResponseEntity<BrandFormDto> createBrand(@RequestBody BrandFormDto brandFormDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(brandService.createBrand(brandFormDto));
    }

    @PutMapping("/brands/{id}")
    public ResponseEntity<BrandFormDto> updateBrand(@PathVariable Integer id, @RequestBody BrandFormDto brandFormDto) {
        return ResponseEntity.ok(brandService.updateBrand(id, brandFormDto));
    }
}
