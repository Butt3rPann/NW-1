package sit.integrated.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "saleItem")
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "model", nullable = false, length = 60)
    private String model;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    @Min(value = 0)
    private Integer price;

    @Column(name = "ramGb")
    @Min(value = 0)
    private Integer ramGb;

    @Column(name = "screenSizeInch", precision = 2, scale = 1)
    @Min(value = 0)
    private BigDecimal screenSizeInch;

    @Column(name = "storageGb")
    @Min(value = 0)
    private Integer storageGb;

    @Column(name = "color", length = 30)
    private String color;

    @ColumnDefault("1")
    @Column(name = "quantity", nullable = false)
    @Min(value = 0)
    private Integer quantity;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "createdOn", nullable = false)
    private Instant createdOn;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updatedOn", nullable = false)
    private Instant updatedOn;

}
