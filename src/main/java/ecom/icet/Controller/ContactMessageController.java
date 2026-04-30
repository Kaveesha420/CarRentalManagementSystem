package ecom.icet.Controller;

import ecom.icet.Model.Entity.ContactMessage;
import ecom.icet.Service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/message")
@CrossOrigin
@RequiredArgsConstructor
public class ContactMessageController {
    private final ContactMessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<ContactMessage> sendMessage(@RequestBody ContactMessage msg) {
        return ResponseEntity.ok(messageService.saveMessage(msg));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ContactMessage>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @PutMapping("/reply/{id}")
    public ResponseEntity<ContactMessage> reply(@PathVariable String id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(messageService.replyToMessage(id, body.get("reply")));
    }
    @GetMapping("/pending-count")
    public long getPendingCount() {
        return messageService.getPendingMessageCount();
    }
    @GetMapping("/customer/{id}")
    public ResponseEntity<List<ContactMessage>> getCustomerMessages(@PathVariable String id) {
        return ResponseEntity.ok(messageService.getMessagesByCustomerId(id));
    }
}