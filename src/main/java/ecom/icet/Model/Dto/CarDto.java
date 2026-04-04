package ecom.icet.Model.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarDto {
    private String id;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "FuelType is required")
    private String fuelType;

    @Min(value = 1, message = "Seating capacity must be at least 1")
    private int seatingCapacity;

    @NotNull(message = "PricePerDay is required")
    @Min(value = 1000, message = "Price must be at least 1000")
    private double pricePerDay;

    private String imagePath;

    @NotBlank(message = "Status is required")
    private String status;

    private String category;

    private String transmission;
}
