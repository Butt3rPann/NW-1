package sit.integrated.backend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import sit.integrated.backend.dtos.BrandFormDto;
import sit.integrated.backend.entities.Brand;
import sit.integrated.backend.exceptions.DuplicateBrandException;
import sit.integrated.backend.repositories.BrandRepository;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ModelMapper modelMapper;

    public void isBrandExists(Integer id) {
        if(!brandRepository.existsById(id)) {
            throw new ResourceNotFoundException("Brand not found for this id :: " + id);
        }
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public Brand getBrandById(Integer id) {
        return brandRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Brand not found for this id :: " + id));
    }

    public BrandFormDto createBrand(BrandFormDto brandFormDto) {
        if (brandRepository.existsBrandsByName(brandFormDto.getName())) {
            throw new DuplicateBrandException("Brand name already exists.");
        }
        brandFormDto.setId(null);
        Brand brand = modelMapper.map(brandFormDto, Brand.class);
        return modelMapper.map(brandRepository.saveAndFlush(brand), BrandFormDto.class);
    }
}
