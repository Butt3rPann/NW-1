package sit.integrated.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.integrated.backend.entities.Buyer;

public interface BuyerRepository extends JpaRepository<Buyer, Integer> {
    boolean existsByEmail(String email);
}
