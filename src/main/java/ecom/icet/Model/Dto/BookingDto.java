package ecom.icet.Model.Dto;

import lombok.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookingDto {
    private String id;

    @NotNull(message = "Pickup date is required")
    @FutureOrPresent(message = "Pickup date must be today or in the future")
    private LocalDate pickupDate;

    @NotNull(message = "Return date is required")
    @FutureOrPresent(message = "Return date must be today or in the future")
    private LocalDate returnDate;

    private Double totalPrice;
    private String bookingStatus;
    private Boolean withDriver;

    @NotNull(message = "Car ID is required")
    private String carId;

    private String customerId;

    @NotNull(message = "User ID is required")
    private String userId;
    // UI show
    private String carName;
    private String customerName;

}
