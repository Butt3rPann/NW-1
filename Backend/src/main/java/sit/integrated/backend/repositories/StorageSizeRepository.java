package sit.integrated.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.integrated.backend.entities.ViewStoragegb;

public interface StorageSizeRepository extends JpaRepository<ViewStoragegb, Integer> {
}
