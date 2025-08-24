package sit.integrated.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @Column(name = "nickname", nullable = false, length = 40)
    private String nickname;

    @Size(max = 60)
    @NotNull
    @Column(name = "fullname", nullable = false, length = 60)
    private String fullname;

    @NotNull
    @Column(name = "createdOn", nullable = false)
    private Instant createdOn;

    @NotNull
    @Column(name = "updatedOn", nullable = false)
    private Instant updatedOn;

}