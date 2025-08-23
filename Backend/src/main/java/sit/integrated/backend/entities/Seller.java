package sit.integrated.backend.entities;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

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

    @CreationTimestamp
    @Column(name = "createdOn", nullable = false, insertable = false, updatable = false)
    private Instant createdOn;

    @UpdateTimestamp
    @Column(name = "updatedOn", nullable = false, insertable = false, updatable = false)
    private Instant updatedOn;

}