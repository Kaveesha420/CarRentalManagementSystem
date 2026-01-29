package ecom.icet.Model.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Payment {
    @Id
    private String id;
    @Column(nullable = false)
    private Double amount;
    @Column(nullable = false)
    private LocalDate paymentDate;
    @Column(nullable = false)
    private String paymentMethod;
    @OneToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "id", nullable = false)
    private Booking booking;
}
