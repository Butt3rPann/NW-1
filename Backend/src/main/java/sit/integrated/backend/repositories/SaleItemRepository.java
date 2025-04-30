package sit.integrated.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.integrated.backend.entities.Saleitem;

public interface SaleItemRepository extends JpaRepository<Saleitem, Integer> {
}
