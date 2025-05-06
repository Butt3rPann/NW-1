package sit.integrated.backend.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.integrated.backend.dtos.BrandDto;
import sit.integrated.backend.entities.Brand;
import sit.integrated.backend.repositories.BrandRepository;
import sit.integrated.backend.utils.ListMapper;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class BrandController {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ListMapper listMapper;

    @GetMapping("/brands")
    public ResponseEntity<List<BrandDto>> getAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        return ResponseEntity.ok(listMapper.mapList(brands,BrandDto.class,new ModelMapper()));
    }
}
