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
import org.springframework.data.domain.Sort;
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
        boolean isBooked = bookingRepository.existsByCarIdAndDateRange(
                bookingDto.getCarId(), bookingDto.getPickupDate(), bookingDto.getReturnDate()
        );
        if (isBooked) throw new RuntimeException("Car is unavailable for selected dates.");

        Booking booking = new Booking();
        Car car = carRepository.findById(bookingDto.getCarId()).orElseThrow();
        User user = userRepository.findById(bookingDto.getUserId()).orElseThrow();

        Customer customer = customerRepository.findByUserId(bookingDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Customer profile not found for this user."));

        booking.setCar(car);
        booking.setCustomer(customer);
        booking.setUser(user);
        booking.setPickupDate(bookingDto.getPickupDate());
        booking.setReturnDate(bookingDto.getReturnDate());
        booking.setWithDriver(bookingDto.getWithDriver());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setBookingStatus("PENDING");

        long days = ChronoUnit.DAYS.between(bookingDto.getPickupDate(), bookingDto.getReturnDate());
        if (days <= 0) days = 1;

        double total = days * car.getPricePerDay();
        if (Boolean.TRUE.equals(bookingDto.getWithDriver())) {
            total += (days * 3000.0);
        }
        booking.setTotalPrice(total);

        // ID Generation
        Booking last = bookingRepository.findFirstByOrderByIdDesc();
        booking.setId(IdGenerator.generateNextId(last != null ? last.getId() : null, "BKG"));

        return mapper.convertValue(bookingRepository.save(booking), BookingDto.class);
    }

    @Override
    public Page<BookingDto> getAllBookings(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        // Page derect map
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

    @Override
    @Transactional
    public BookingDto assignDriver(String bookingId, String driverId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        booking.setDriver(driver);
        booking.setBookingStatus("CONFIRMED");

        Booking updated = bookingRepository.save(booking);


        auditLogService.logAction("UPDATE", "Driver " + driverId + " assigned to booking " + bookingId);

        return mapper.convertValue(updated, BookingDto.class);
    }
}
