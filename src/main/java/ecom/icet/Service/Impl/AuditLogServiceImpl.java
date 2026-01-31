package ecom.icet.Service.Impl;

import ecom.icet.Model.Entity.AuditLog;
import ecom.icet.Repository.AuditLogRepository;
import ecom.icet.Service.AuditLogService;
import ecom.icet.Util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void logAction(String action, String description) {
        AuditLog log = new AuditLog();

        log.setAction(action);
        log.setDescription(description);
        log.setTimestamp(LocalDateTime.now());
        log.setPerformedBy("ADMIN");

        AuditLog lastLog = auditLogRepository.findFirstByOrderByIdDesc();
        String lastId = (lastLog != null) ? lastLog.getId() : null;
        log.setId(IdGenerator.generateNextId(lastId, "LOG"));

        auditLogRepository.save(log);
    }
}
