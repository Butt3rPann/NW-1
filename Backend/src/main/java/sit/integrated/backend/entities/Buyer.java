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

@Getter
@Setter
@Entity
@Table(name = "buyers")
public class Buyer {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @NotNull
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Size(max = 40)
    @NotNull
    @Column(name = "nick_name", nullable = false, length = 40)
    private String nickName;

    @Size(max = 60)
    @NotNull
    @Column(name = "full_name", nullable = false, length = 60)
    private String fullName;

    @CreationTimestamp
    @Column(name = "createdOn", nullable = false, insertable = false, updatable = false)
    private Instant createdOn;

    @UpdateTimestamp
    @Column(name = "updatedOn", nullable = false, insertable = false, updatable = false)
    private Instant updatedOn;

}