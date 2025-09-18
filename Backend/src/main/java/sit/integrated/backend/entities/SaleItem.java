package sit.integrated.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "sale_items")
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 60)
    @NotNull
    @Column(name = "model", nullable = false, length = 60)
    private String model;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brandId", nullable = false)
    private Brand brand;

    @NotNull
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "ramGb")
    private Integer ramGb;

    @Column(name = "screenSizeInch", precision = 4, scale = 2)
    private BigDecimal screenSizeInch;

    @Column(name = "storageGb")
    private Integer storageGb;

    @Size(max = 40)
    @Column(name = "color", length = 40)
    private String color;

    @ColumnDefault("1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @CreationTimestamp
    @Column(name = "createdOn", nullable = false, insertable = false, updatable = false)
    private Instant createdOn;

    @UpdateTimestamp
    @Column(name = "updatedOn", nullable = false, insertable = false, updatable = false)
    private Instant updatedOn;

    public void setQuantity(Integer quantity) {
        if (quantity == null || quantity < 0) {
            this.quantity = 1;
        } else {
            this.quantity = quantity;
        }
    }
}