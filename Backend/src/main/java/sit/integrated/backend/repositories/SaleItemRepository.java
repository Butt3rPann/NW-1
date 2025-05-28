package sit.integrated.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sit.integrated.backend.entities.SaleItem;

import java.util.List;

public interface SaleItemRepository extends JpaRepository<SaleItem, Integer> {
    @Query("SELECT s FROM SaleItem s JOIN s.brand b WHERE b.name IN :brands")
    Page<SaleItem> findByBrands(@Param("brands") List<String> brands, Pageable pageable);
}
