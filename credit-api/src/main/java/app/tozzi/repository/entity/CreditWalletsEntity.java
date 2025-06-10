package app.tozzi.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "c_wallet", uniqueConstraints = {
        @UniqueConstraint(columnNames = "USER_ID")
})
public class CreditWalletsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "BALANCE", precision = 15, scale = 2)
    private BigDecimal balance;

}
