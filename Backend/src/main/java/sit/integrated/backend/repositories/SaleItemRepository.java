package sit.integrated.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sit.integrated.backend.entities.SaleItem;

import java.util.List;

public interface SaleItemRepository extends JpaRepository<SaleItem, Integer> {
    @Query("""
           SELECT s FROM SaleItem s JOIN s.brand b
           WHERE (:brands IS NULL OR b.name IN :brands)
           AND (:lower IS NULL OR s.price >= :lower)
           AND (:upper IS NULL OR s.price <= :upper)
           AND (:storages IS NULL OR s.storageGb IN :storages OR (:hasNull = true AND s.storageGb IS NULL))
           AND (:keyword IS NULL
                OR s.description LIKE CONCAT('%', :keyword, '%')
                OR s.model LIKE CONCAT('%', :keyword, '%')
                OR s.color LIKE CONCAT('%', :keyword, '%')
                )
           """)
    Page<SaleItem> findFilteredItems(@Param("brands") List<String> brands,
                                     @Param("storages") List<Integer> filterStorages,
                                     @Param("hasNull") boolean hasNull,
                                     @Param("lower") Integer filterPriceLower,
                                     @Param("upper") Integer filterPriceUpper,
                                     @Param("keyword") String keyword, Pageable pageable);
}
