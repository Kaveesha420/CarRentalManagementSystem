package ecom.icet.Controller;

import ecom.icet.Model.Dto.PaymentDto;
import ecom.icet.Service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@CrossOrigin
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/add")
    public ResponseEntity<PaymentDto> addPayment(@Valid @RequestBody PaymentDto paymentDto) {
        return ResponseEntity.ok(paymentService.addPayment(paymentDto));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<PaymentDto>> getAllPayments(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(paymentService.getAllPayments(page, size));
    }

    @GetMapping("/getPaymentByBookingId/{id}")
    public ResponseEntity<PaymentDto> getPaymentByBookingId(@PathVariable String id) {
        return ResponseEntity.ok(paymentService.getPaymentByBookingId(id));
    }

}
