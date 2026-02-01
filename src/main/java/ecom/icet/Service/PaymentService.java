package ecom.icet.Service;

import ecom.icet.Model.Dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    PaymentDto addPayment(PaymentDto paymentDto);
    List<PaymentDto> getAllPayments(int page,int size);
    PaymentDto getPaymentByBookingId(String bookingId);
}
