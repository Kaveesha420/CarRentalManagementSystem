package ecom.icet.Service;

public interface AuditLogService {
    void logAction(String action, String description);
}
