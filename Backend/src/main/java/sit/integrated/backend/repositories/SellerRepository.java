package sit.integrated.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.integrated.backend.entities.Seller;

public interface SellerRepository extends JpaRepository<Seller, Integer> {
    @Query("SELECT s.nickName FROM Seller s WHERE s.userId = ?1")
    String getNickname(Integer id);
}
