package sit.integrated.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.integrated.backend.entities.Brand;
import sit.integrated.backend.repositories.BrandRepository;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }
}
