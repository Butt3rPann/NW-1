package sit.integrated.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import sit.integrated.backend.entities.User;
import sit.integrated.backend.utils.UserStatus;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.status = ?2 WHERE u.email = ?1")
    int updateStatusByEmail(String email, UserStatus status);

    Optional<User> findByEmail(String email);
}
