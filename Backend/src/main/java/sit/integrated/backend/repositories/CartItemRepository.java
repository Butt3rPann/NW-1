package sit.integrated.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sit.integrated.backend.entities.CartItem;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    @Query("SELECT c.id FROM CartItem c WHERE c.user.id = ?1 AND c.saleItem.id = ?2")
    Integer findIdByUserAndSaleItem(Integer userId, Integer saleItemId);

    @Query("SELECT c.quantity FROM CartItem c WHERE c.id = ?1")
    int getQuantityById(Integer cartId);

    @Modifying
    @Query("UPDATE CartItem c SET c.quantity = ?2 WHERE c.id = ?1")
    int updateQuantity(Integer cartItemid, Integer newQty);

    @Query("SELECT c FROM CartItem c WHERE c.user.id = ?1")
    List<CartItem> getCartItemsByUserId(Integer userId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.user.id = ?1 AND c.saleItem.id = ?2")
    int deleteByUserAndSaleItem(Integer userId, Integer saleItemId);
}