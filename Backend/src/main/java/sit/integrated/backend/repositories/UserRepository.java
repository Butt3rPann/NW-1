package sit.integrated.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sit.integrated.backend.entities.User;
import sit.integrated.backend.utils.UserStatus;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.status = ?2 WHERE u.email = ?1")
    int updateStatusByEmail(String email, UserStatus status);

    Optional<User> findByEmail(String email);

    @Query("SELECT s.user FROM SaleItem s WHERE s.id = ?1")
    User getUserBySaleItemId(Integer saleItemId);

    @Query("SELECT DISTINCT s.user FROM CartItem c JOIN SaleItem s WHERE c.user.id = ?1")
    List<User> getDistinctSellerFromCartItems(Integer userId);
}
