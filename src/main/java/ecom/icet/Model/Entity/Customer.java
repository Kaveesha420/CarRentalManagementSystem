package ecom.icet.Model.Entity;

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
@SQLDelete(sql = "UPDATE customers SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted = false")
public class Customer {

    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String contactNo;
    @Column(unique = true, nullable = false)
    private String nic;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private boolean isDeleted = false;

}