package ecom.icet.Model.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@SQLDelete(sql = "UPDATE drivers SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
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

    @Column(nullable = false)
    private boolean isDeleted = false;
}
