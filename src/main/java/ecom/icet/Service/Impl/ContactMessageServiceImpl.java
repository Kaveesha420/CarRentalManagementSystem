package ecom.icet.Service.Impl;

import ecom.icet.Model.Entity.ContactMessage;
import ecom.icet.Repository.ContactMessageRepository;
import ecom.icet.Service.AuditLogService;
import ecom.icet.Service.ContactMessageService;
import ecom.icet.Util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageRepository repository;
    private final AuditLogService auditLogService;

    @Override
    public ContactMessage saveMessage(ContactMessage message) {

        long count = repository.count();
        message.setId(IdGenerator.generateNextId(count > 0 ? "MSG" + count : null, "MSG"));

        message.setTimestamp(LocalDateTime.now());
        message.setStatus("PENDING");

        ContactMessage saved = repository.save(message);
        auditLogService.logAction("CONTACT", "Customer " + saved.getCustomerName() + " sent a message.");
        return saved;
    }

    @Override
    public List<ContactMessage> getAllMessages() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public List<ContactMessage> getMessagesByCustomerId(String customerId) {
        return repository.findByCustomerIdOrderByTimestampDesc(customerId);
    }

    @Override
    public ContactMessage replyToMessage(String id, String reply) {
        Optional<ContactMessage> optional = repository.findById(id);
        if (optional.isPresent()) {
            ContactMessage message = optional.get();
            message.setAdminReply(reply);
            message.setReplyTimestamp(LocalDateTime.now());
            message.setStatus("REPLIED");

            ContactMessage updated = repository.save(message);
            auditLogService.logAction("REPLY", "Admin replied to message: " + id);
            return updated;
        }
        return null;
    }

    @Override
    public long getPendingMessageCount() {
        return repository.countByStatus("PENDING");
    }
}