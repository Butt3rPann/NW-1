package sit.integrated.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.integrated.backend.entities.PasswordReset;
import sit.integrated.backend.entities.User;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Integer> {
    Optional<PasswordReset> findByResetToken(String resetToken);
    void deleteByUser(User user);
}
