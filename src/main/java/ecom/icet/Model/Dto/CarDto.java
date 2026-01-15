package ecom.icet.Model.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarDto {
    private Long id;
    private String brand;
    private String model;
    private String fuelType;
    private int seatingCapacity;
    private double pricePerDay;
    private String imagePath;
    private String status;
}
