package ecom.icet.Model.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDto {
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double amount;

    private LocalDate paymentDate;

    @NotBlank(message = "Payment Methods is required")
    private String paymentMethod;

    @NotBlank(message = "Booking Id is required")
    private Long bookingId;

}
