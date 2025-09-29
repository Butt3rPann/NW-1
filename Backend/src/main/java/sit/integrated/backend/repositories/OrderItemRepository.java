package sit.integrated.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.integrated.backend.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
