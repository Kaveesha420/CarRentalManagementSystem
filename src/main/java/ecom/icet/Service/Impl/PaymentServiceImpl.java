package ecom.icet.Service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecom.icet.Model.Dto.PaymentDto;
import ecom.icet.Model.Entity.Booking;
import ecom.icet.Model.Entity.Payment;
import ecom.icet.Repository.BookingRepository;
import ecom.icet.Repository.PaymentRepository;
import ecom.icet.Service.PaymentService;
import ecom.icet.Util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final ObjectMapper mapper;

    @Override
    public PaymentDto addPayment(PaymentDto paymentDto) {
        Payment payment = new Payment();

        Booking booking = bookingRepository.findById(paymentDto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        payment.setBooking(booking);
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentMethod(paymentDto.getPaymentMethod());

        Payment lastPayment = paymentRepository.findFirstByOrderByIdDesc();
        String lastId = (lastPayment != null) ? lastPayment.getId() : null;
        payment.setId(IdGenerator.generateNextId(lastId, "PAY"));

        Payment savedPayment = paymentRepository.save(payment);
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
