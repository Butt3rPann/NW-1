package sit.integrated.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.integrated.backend.entities.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
}
