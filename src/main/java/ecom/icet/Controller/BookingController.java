package ecom.icet.Controller;

import ecom.icet.Model.Dto.BookingDto;
import ecom.icet.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
@CrossOrigin
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/add")
    public ResponseEntity<BookingDto> addBooking(@RequestBody BookingDto bookingDto) {
        return ResponseEntity.ok(bookingService.addBooking(bookingDto));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/getCustomerById/{id}")
    public ResponseEntity<List<BookingDto>> getBookingsByCustomerId(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingsByCustomerId(id));
    }

    @PutMapping("/updateStatus/{id}/{status}")
    public ResponseEntity<BookingDto> updateBookingStatus(@PathVariable Long id, @PathVariable String status) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, status));
    }
}

