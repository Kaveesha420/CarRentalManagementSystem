package ecom.icet.Repository;

import ecom.icet.Model.Entity.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, String> {

    long countByStatus(String status);
    //for check history
    List<ContactMessage> findByCustomerIdOrderByTimestampDesc(String customerId);

    // show message in admin
    List<List<ContactMessage>> findAllByOrderByTimestampDesc();
}