package ecom.icet.Service;

import ecom.icet.Model.Dto.BookingDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookingDto bookingDto);
    Page<BookingDto> getAllBookings(int page, int size);
    List<BookingDto> getBookingsByCustomerId(String customerId);
    BookingDto updateBookingStatus(String id,String status);
    BookingDto assignDriver(String bookingId, String driverId);
}
