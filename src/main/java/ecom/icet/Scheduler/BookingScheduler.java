package ecom.icet.Scheduler;

import ecom.icet.Model.Entity.Booking;
import ecom.icet.Model.Entity.Car;
import ecom.icet.Model.Entity.Driver;
import ecom.icet.Repository.BookingRepository;
import ecom.icet.Repository.CarRepository;
import ecom.icet.Repository.DriverRepository;
import ecom.icet.Service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingScheduler {

    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final DriverRepository driverRepository;
    private final AuditLogService auditLogService;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cancelExpiredBookings() {
        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(15);
        List<Booking> expiredBookings = bookingRepository.findByBookingStatusAndCreatedAtBefore("PENDING", expiryTime);

        for (Booking booking : expiredBookings) {
            booking.setBookingStatus("CANCELLED");
            bookingRepository.save(booking);
            auditLogService.logAction("AUTO_CANCEL", "Booking " + booking.getId() + " auto-cancelled.");
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void updateCarAndDriverStatus() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        List<Booking> startingToday = bookingRepository.findByPickupDateAndBookingStatus(today, "CONFIRMED");
        for (Booking booking : startingToday) {
            processTripStart(booking);
        }

        List<Booking> endedYesterday = bookingRepository.findByReturnDateAndBookingStatus(yesterday, "CONFIRMED");
        for (Booking booking : endedYesterday) {
            processTripEnd(booking);
        }
    }

    String onTrip = "ON_TRIP";
    String statusUpdate = "STATUS_UPDATE";

    private void processTripStart(Booking booking) {

        Car car = booking.getCar();
        if (car != null && !onTrip.equals(car.getStatus())) {
            car.setStatus(onTrip);
            carRepository.save(car);
            auditLogService.logAction(statusUpdate, "Car " + car.getId() + " marked as ON_TRIP");
        }

        if (Boolean.TRUE.equals(booking.getWithDriver()) && booking.getDriver() != null) {
            Driver driver = booking.getDriver();
            if (!onTrip.equals(driver.getStatus())) {
                driver.setStatus(onTrip);
                driverRepository.save(driver);
                auditLogService.logAction(statusUpdate, "Driver " + driver.getId() + " marked as ON_TRIP");
            }
        }
    }

    private void processTripEnd(Booking booking) {
        Car car = booking.getCar();
        if (car != null && onTrip.equals(car.getStatus())) {
            car.setStatus("AVAILABLE");
            carRepository.save(car);
            auditLogService.logAction(statusUpdate, "Car " + car.getId() + " marked as AVAILABLE");
        }

        if (Boolean.TRUE.equals(booking.getWithDriver()) && booking.getDriver() != null) {
            Driver driver = booking.getDriver();
            if (onTrip.equals(driver.getStatus())) {
                driver.setStatus("AVAILABLE");
                driverRepository.save(driver);
                auditLogService.logAction(statusUpdate, "Driver " + driver.getId() + " marked as AVAILABLE");
            }
        }
    }
}