package sit.integrated.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "view_storageGb")
public class ViewStoragegb {

    @Id
    @Column(name = "storageGb")
    private Integer storageGb;

}
