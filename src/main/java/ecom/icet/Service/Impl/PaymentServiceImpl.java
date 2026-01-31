package ecom.icet.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.icet.Model.Dto.PaymentDto;
import ecom.icet.Model.Entity.Booking;
import ecom.icet.Model.Entity.Payment;
import ecom.icet.Repository.BookingRepository;
import ecom.icet.Repository.PaymentRepository;
import ecom.icet.Service.AuditLogService;
import ecom.icet.Service.PaymentService;
import ecom.icet.Util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final ObjectMapper mapper;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public PaymentDto addPayment(PaymentDto paymentDto) {
        Optional<Payment> existingPayment = paymentRepository.findByBookingId(paymentDto.getBookingId());
        if (existingPayment.isPresent()) {
            throw new IllegalArgumentException("Payment already exists for Booking ID: " + paymentDto.getBookingId());
        }

        Payment payment = new Payment();

        Booking booking = bookingRepository.findById(paymentDto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if ("CANCELLED".equals(booking.getBookingStatus())) {
            throw new IllegalArgumentException("Booking is expired/cancelled. Please make a new booking.");
        }

        payment.setBooking(booking);
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentMethod(paymentDto.getPaymentMethod());

        booking.setBookingStatus("CONFIRMED");
        bookingRepository.save(booking);

        Payment lastPayment = paymentRepository.findFirstByOrderByIdDesc();
        String lastId = (lastPayment != null) ? lastPayment.getId() : null;
        payment.setId(IdGenerator.generateNextId(lastId, "PAY"));

        Payment savedPayment = paymentRepository.save(payment);
        auditLogService.logAction("CREATE", "Payment processed: " + savedPayment.getId() + " for Booking " + savedPayment.getBooking().getId());
        return mapper.convertValue(savedPayment, PaymentDto.class);
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        List<PaymentDto> dtoList = new ArrayList<>();

        for (Payment payment : payments){
            dtoList.add(mapper.convertValue(payment, PaymentDto.class));
        }
        return dtoList;
    }

    @Override
    public PaymentDto getPaymentByBookingId(String bookingId) {
        return paymentRepository.findByBookingId(bookingId)
                .map(payment -> mapper.convertValue(payment, PaymentDto.class))
                .orElse(null);
    }
}
