package ecom.icet.Model.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AuditLog {
    @Id
    private String id;
    private String action;
    private String description;
    private LocalDateTime timestamp;

    private String performedBy;
}
