package ecom.icet.Controller;

import ecom.icet.Model.Dto.BookingDto;
import ecom.icet.Service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<BookingDto> addBooking(@Valid @RequestBody BookingDto bookingDto) {
        return ResponseEntity.ok(bookingService.addBooking(bookingDto));
    }

    @GetMapping("/getAll")
    public ResponseEntity<Page<BookingDto>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookingService.getAllBookings(page, size));
    }
    @GetMapping("/getCustomerById/{id}")
    public ResponseEntity<List<BookingDto>> getBookingsByCustomerId(@PathVariable String id) {
        return ResponseEntity.ok(bookingService.getBookingsByCustomerId(id));
    }

    @PutMapping("/updateStatus/{id}/{status}")
    public ResponseEntity<BookingDto> updateBookingStatus(@PathVariable String id, @PathVariable String status) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, status));
    }

    // BookingController_5.java ඇතුළත
    @PutMapping("/assignDriver/{bookingId}/{driverId}")
    public ResponseEntity<BookingDto> assignDriver(@PathVariable String bookingId, @PathVariable String driverId) {
        return ResponseEntity.ok(bookingService.assignDriver(bookingId, driverId));
    }
}

