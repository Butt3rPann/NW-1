package sit.integrated.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.integrated.backend.entities.Order;
import sit.integrated.backend.utils.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findOrdersByUserId(Integer UserId, Pageable pageable);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi WHERE oi.saleItem.user.id = ?1")
    Page<Order> findOrdersBySeller(Integer sellerId, Pageable pageable);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi WHERE oi.saleItem.user.id = ?1 AND o.orderStatus = ?2")
    Page<Order> findOrdersBySellerAndOrderStatus(Integer sellerId, OrderStatus status, Pageable pageable);
}
