package ecom.icet.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.icet.Model.Dto.BookingDto;
import ecom.icet.Model.Entity.*;
import ecom.icet.Repository.*;
import ecom.icet.Service.AuditLogService;
import ecom.icet.Service.BookingService;
import ecom.icet.Util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public BookingDto addBooking(BookingDto bookingDto) {

        boolean isCarBooked = bookingRepository.existsByCarIdAndDateRange(
                bookingDto.getCarId(), bookingDto.getPickupDate(), bookingDto.getReturnDate()
        );

        if (isCarBooked) {
            throw new IllegalArgumentException("Car is already booked for the selected dates!");
        }

        if (Boolean.TRUE.equals(bookingDto.getWithDriver()) && bookingDto.getDriverId() != null) {
            boolean isDriverBooked = bookingRepository.existsByDriverIdAndDateRange(
                    bookingDto.getDriverId(), bookingDto.getPickupDate(), bookingDto.getReturnDate()
            );
            if (isDriverBooked) {
                throw new IllegalArgumentException("Selected Driver is unavailable for these dates!");
            }
        }

        Booking booking = new Booking();

        Car car = carRepository.findById(bookingDto.getCarId()).orElseThrow(() -> new RuntimeException("Car not found"));
        Customer customer = customerRepository.findById(bookingDto.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer not found"));
        User user = userRepository.findById(bookingDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        booking.setCar(car);
        booking.setCustomer(customer);
        booking.setUser(user);

        if (bookingDto.getDriverId() != null && !bookingDto.getDriverId().isEmpty()) {
            Driver driver = driverRepository.findById(bookingDto.getDriverId()).orElse(null);
            booking.setDriver(driver);
        }

        booking.setPickupDate(bookingDto.getPickupDate());
        booking.setReturnDate(bookingDto.getReturnDate());
        booking.setWithDriver(bookingDto.getWithDriver());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setBookingStatus("PENDING");

        long days = ChronoUnit.DAYS.between(bookingDto.getPickupDate(), bookingDto.getReturnDate());
        booking.setTotalPrice(days * car.getPricePerDay());

        Booking lastBooking = bookingRepository.findFirstByOrderByIdDesc();
        String lastId = (lastBooking != null) ? lastBooking.getId() : null;
        booking.setId(IdGenerator.generateNextId(lastId, "BKG"));

        Booking savedBooking = bookingRepository.save(booking);
        auditLogService.logAction("CREATE", "New Booking placed: " + savedBooking.getId() + " for Car " + savedBooking.getCar().getId());
        return mapper.convertValue(savedBooking, BookingDto.class);

    }

    @Override
    public Page<BookingDto> getAllBookings(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // Page එක කෙලින්ම Map කරලා යවනවා
        return bookingRepository.findAll(pageable).map(booking -> mapper.convertValue(booking, BookingDto.class));
    }

    @Override
    public List<BookingDto> getBookingsByCustomerId(String customerId) {
        List<Booking> bookings = bookingRepository.findByCustomerId(customerId);
        List<BookingDto> dtoList = new ArrayList<>();

        for (Booking booking: bookings){
            dtoList.add(mapper.convertValue(booking, BookingDto.class));
        }
        return dtoList;
    }

    @Override
    public BookingDto updateBookingStatus(String id, String status) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if (bookingOptional.isPresent()){
            Booking booking = bookingOptional.get();
            booking.setBookingStatus(status);
            Booking updated = bookingRepository.save(booking);
            auditLogService.logAction("UPDATE", "Booking " + id + " status changed to " + status);
            return mapper.convertValue(updated, BookingDto.class);
        }
        return null;
    }
}
