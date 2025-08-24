package sit.integrated.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.integrated.backend.entities.Seller;

public interface SellerRepository extends JpaRepository<Seller,Integer> {
}
