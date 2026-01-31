package ecom.icet.Repository;

import ecom.icet.Model.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,String>{
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.car.id = :carId " +
            "AND b.bookingStatus NOT IN ('CANCELLED', 'COMPLETED') " +
            "AND ((:pickupDate BETWEEN b.pickupDate AND b.returnDate) " +
            "OR (:returnDate BETWEEN b.pickupDate AND b.returnDate) " +
            "OR (b.pickupDate BETWEEN :pickupDate AND :returnDate))")
    boolean existsByCarIdAndDateRange(@Param("carId") String carId,
                                      @Param("pickupDate") LocalDate pickupDate,
                                      @Param("returnDate") LocalDate returnDate);

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.driver.id = :driverId " +
            "AND b.bookingStatus NOT IN ('CANCELLED', 'COMPLETED') " +
            "AND ((:pickupDate BETWEEN b.pickupDate AND b.returnDate) " +
            "OR (:returnDate BETWEEN b.pickupDate AND b.returnDate) " +
            "OR (b.pickupDate BETWEEN :pickupDate AND :returnDate))")
    boolean existsByDriverIdAndDateRange(@Param("driverId") String driverId,
                                         @Param("pickupDate") LocalDate pickupDate,
                                         @Param("returnDate") LocalDate returnDate);

    List<Booking> findByBookingStatusAndCreatedAtBefore(String status, LocalDateTime timeLimit);

    Booking findFirstByOrderByIdDesc();
    List<Booking> findByCustomerId(String customerId);

    List<Booking> findByPickupDateAndBookingStatus(LocalDate today, String confirmed);
    List<Booking> findByReturnDateAndBookingStatus(LocalDate yesterday, String confirmed);
}

