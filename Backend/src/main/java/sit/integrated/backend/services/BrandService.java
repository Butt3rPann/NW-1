package sit.integrated.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import sit.integrated.backend.entities.Brand;
import sit.integrated.backend.repositories.BrandRepository;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

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
}
