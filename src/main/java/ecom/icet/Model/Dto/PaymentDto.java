package ecom.icet.Model.Dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDto {
    private Long id;
    private Double amount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private Long bookingId;

}
