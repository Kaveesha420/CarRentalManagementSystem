package ecom.icet.Model.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ContactMessage {
    @Id
    private String id;
    private String customerId;
    private String customerName;
    private String email;
    @Column(length = 1000)
    private String message;
    @Column(length = 1000)
    private String adminReply;
    private LocalDateTime timestamp;
    private LocalDateTime replyTimestamp;
    private String status; // PENDING, REPLIED
}