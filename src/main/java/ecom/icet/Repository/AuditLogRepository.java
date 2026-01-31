package ecom.icet.Repository;

import ecom.icet.Model.Entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog,String> {
    AuditLog findFirstByOrderByIdDesc();
}
