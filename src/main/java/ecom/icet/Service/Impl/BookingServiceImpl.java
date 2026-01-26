package ecom.icet.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.icet.Model.Dto.BookingDto;
import ecom.icet.Model.Entity.*;
import ecom.icet.Repository.*;
import ecom.icet.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final DriverRepository driverRepository;
    private final ObjectMapper mapper;

    @Override
    public BookingDto addBooking(BookingDto bookingDto) {
        Booking booking = new Booking();

        Car car = carRepository.findById(bookingDto.getCarId()).orElseThrow(()->new RuntimeException("Car not found"));
        Customer customer = customerRepository.findById(bookingDto.getCustomerId()).orElseThrow(()->new RuntimeException("Customer not found"));
        User user = userRepository.findById(bookingDto.getUserId()).orElseThrow(()->new RuntimeException("User not found"));

        booking.setCar(car);
        booking.setCustomer(customer);
        booking.setUser(user);

        if (bookingDto.getDriverId() != null){
            Driver driver = driverRepository.findById(bookingDto.getDriverId()).orElse(null);
            booking.setDriver(driver);
        }

        booking.setPickupDate(bookingDto.getPickupDate());
        booking.setReturnDate(bookingDto.getReturnDate());
        booking.setWithDriver(bookingDto.getWithDriver());
        booking.setBookingStatus("PENDING");

        long days = ChronoUnit.DAYS.between(bookingDto.getPickupDate(),bookingDto.getReturnDate());
        Double totalPrice = days * car.getPricePerDay();
        booking.setTotalPrice(totalPrice);

        Booking savedBooking = bookingRepository.save(booking);
        return mapper.convertValue(savedBooking, BookingDto.class);

    }

    @Override
    public List<BookingDto> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingDto> dtoList = new ArrayList<>();

        for (Booking booking:bookings){
            dtoList.add(mapper.convertValue(booking, BookingDto.class));
        }
        return dtoList;
    }

    @Override
    public List<BookingDto> getBookingsByCustomerId(Long customerId) {
        List<Booking> bookings = bookingRepository.findByCustomerId(customerId);
        List<BookingDto> dtoList = new ArrayList<>();

        for (Booking booking: bookings){
            dtoList.add(mapper.convertValue(booking, BookingDto.class));
        }
        return dtoList;
    }

    @Override
    public BookingDto updateBookingStatus(Long id, String status) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if (bookingOptional.isPresent()){
            Booking booking = bookingOptional.get();
            booking.setBookingStatus(status);
            Booking updated = bookingRepository.save(booking);
            return mapper.convertValue(updated, BookingDto.class);
        }
        return null;
    }
}
