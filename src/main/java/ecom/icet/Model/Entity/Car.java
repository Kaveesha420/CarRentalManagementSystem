package ecom.icet.Model.Entity;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@SQLDelete(sql = "UPDATE car SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
public class Car {

    @Id
    private String id;
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
    @Column(nullable = false)
    private boolean isDeleted = false;
    private String category;
    private String transmission;
}
