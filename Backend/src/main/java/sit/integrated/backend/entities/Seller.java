package sit.integrated.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "sellers")
public class Seller {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @NotNull
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Size(max = 20)
    @NotNull
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Size(max = 50)
    @NotNull
    @Column(name = "bank_account", nullable = false, length = 50)
    private String bankAccount;

    @Size(max = 100)
    @NotNull
    @Column(name = "bank_name", nullable = false, length = 100)
    private String bankName;

    @Size(max = 25)
    @NotNull
    @Column(name = "id_card_number", nullable = false, length = 25)
    private String idCardNumber;

    @CreationTimestamp
    @Column(name = "createdOn", nullable = false, insertable = false, updatable = false)
    private Instant createdOn;

    @UpdateTimestamp
    @Column(name = "updatedOn", nullable = false)
    private Instant updatedOn;
}
