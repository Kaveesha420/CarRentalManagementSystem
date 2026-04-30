package ecom.icet.Service;

import ecom.icet.Model.Entity.ContactMessage;
import java.util.List;

public interface ContactMessageService {
    ContactMessage saveMessage(ContactMessage message);
    List<ContactMessage> getAllMessages();
    List<ContactMessage> getMessagesByCustomerId(String customerId);
    ContactMessage replyToMessage(String id, String reply);
    long getPendingMessageCount();
}