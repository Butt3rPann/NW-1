package sit.integrated.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.integrated.backend.entities.Buyer;

public interface BuyerRepository extends JpaRepository<Buyer, Integer> {
    @Query("SELECT b.nickName FROM Buyer b WHERE b.userId = ?1")
    String getNickname(Integer id);
}
