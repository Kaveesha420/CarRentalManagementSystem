package ecom.icet.Service;

import ecom.icet.Model.Dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookingDto bookingDto);
    List<BookingDto> getAllBookings(int page,int size);
    List<BookingDto> getBookingsByCustomerId(String customerId);
    BookingDto updateBookingStatus(String id,String status);
}
