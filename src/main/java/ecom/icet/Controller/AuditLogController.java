package ecom.icet.Controller;

import ecom.icet.Model.Entity.AuditLog;
import ecom.icet.Repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/audit")
@CrossOrigin
@RequiredArgsConstructor
public class AuditLogController {
    private final AuditLogRepository auditLogRepository;

    @GetMapping("/recent")
    public List<AuditLog> getRecentLogs() {
        // Get Last 10 logs
        return auditLogRepository.findAll().stream()
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .limit(10).toList();
    }
}