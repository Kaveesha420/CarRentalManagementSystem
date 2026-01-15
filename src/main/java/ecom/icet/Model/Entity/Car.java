package ecom.icet.Model.Entity;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private String fuelType;
    @Column(nullable = false)
    private int seatingCapacity;
    @Column(nullable = false)
    private double pricePerDay;
    private String imagePath;

    @Column(nullable = false)
    private String status;
}
