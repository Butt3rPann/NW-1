package sit.integrated.backend.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.integrated.backend.dtos.BrandDto;
import sit.integrated.backend.entities.Brand;
import sit.integrated.backend.services.BrandService;
import sit.integrated.backend.utils.ListMapper;

import java.util.List;

@RestController
@RequestMapping("/v1")
//@CrossOrigin(origins = "http://localhost:5173")
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
}
