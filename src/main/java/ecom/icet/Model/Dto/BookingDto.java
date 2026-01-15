package ecom.icet.Model.Dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookingDto {
    private Long id;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private Double totalPrice;
    private String bookingStatus;
    private Boolean withDriver;

    private Long carId;
    private Long customerId;
    private Long driverId;
    private Long userId;
}
