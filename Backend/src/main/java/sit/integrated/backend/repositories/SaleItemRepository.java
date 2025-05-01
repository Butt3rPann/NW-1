package sit.integrated.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.integrated.backend.entities.SaleItem;

public interface SaleItemRepository extends JpaRepository<SaleItem, Integer> {
}
