package ecom.icet.Model.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Driver {

    @Id
    private String id;
    private String name;
    @Column(unique = true, nullable = false)
    private String licenseNo;
    @Column(nullable = false)
    private String contactNo;

    @Column(nullable = false)
    private String status;
}
