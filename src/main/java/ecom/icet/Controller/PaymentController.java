package ecom.icet.Controller;

import ecom.icet.Model.Dto.PaymentDto;
import ecom.icet.Service.PaymentService;
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
    public ResponseEntity<PaymentDto> addPayment(@RequestBody PaymentDto paymentDto) {
        return ResponseEntity.ok(paymentService.addPayment(paymentDto));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/getPaymentByBookingId/{id}")
    public ResponseEntity<PaymentDto> getPaymentByBookingId(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentByBookingId(id));
    }

}
