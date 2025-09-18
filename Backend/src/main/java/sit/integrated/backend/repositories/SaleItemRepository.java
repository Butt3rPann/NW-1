package sit.integrated.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import sit.integrated.backend.entities.SaleItem;


public interface SaleItemRepository extends JpaRepository<SaleItem, Integer>, JpaSpecificationExecutor<SaleItem> {
    @Query("SELECT sa FROM SaleItem sa WHERE sa.seller.userId = ?1 ORDER BY sa.createdOn ASC, sa.id ASC")
    Page<SaleItem> getSaleItemsBySeller(Integer sellerId, Pageable pageable);
}
