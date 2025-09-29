package sit.integrated.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sit.integrated.backend.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findOrdersByUserId(Integer UserId, Pageable pageable);
}
