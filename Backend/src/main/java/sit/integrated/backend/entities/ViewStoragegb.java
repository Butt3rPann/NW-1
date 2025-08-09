package sit.integrated.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "view_storagegb")
public class ViewStoragegb {

    @Id
    @Column(name = "storageGb")
    private Integer storageGb;

}