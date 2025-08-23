package sit.integrated.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "seller")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Size(max = 20)
    @NotNull
    @Column(name = "mobile_number", nullable = false, length = 20)
    private String mobileNumber;

    @Size(max = 50)
    @NotNull
    @Column(name = "bank_account_number", nullable = false, length = 50)
    private String bankAccountNumber;

    @Size(max = 100)
    @NotNull
    @Column(name = "bank_name", nullable = false, length = 100)
    private String bankName;

    @Size(max = 25)
    @NotNull
    @Column(name = "national_id", nullable = false, length = 25)
    private String nationalId;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdOn", nullable = false)
    private Instant createdOn;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updatedOn", nullable = false)
    private Instant updatedOn;

}