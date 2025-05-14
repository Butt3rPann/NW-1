package sit.integrated.backend.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        List<Brand> brands = brandService.getAllBrands();
        return ResponseEntity.ok(listMapper.mapList(brands,BrandDto.class, modelMapper));
    }

    @GetMapping("/brands/{id}")
    public ResponseEntity<BrandFormDto> getBrand(@PathVariable Integer id) {
        Brand brand = brandService.getBrandById(id);
        BrandFormDto brandFormDto = modelMapper.map(brand, BrandFormDto.class);
        brandFormDto.setSaleItemCount(brand.getSaleItems().size());
        return ResponseEntity.ok(brandFormDto);
    }

    @PostMapping("/brands")
    public ResponseEntity<BrandFormDto> createBrand(@RequestBody BrandFormDto brandFormDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(brandService.createBrand(brandFormDto));
    }
}
