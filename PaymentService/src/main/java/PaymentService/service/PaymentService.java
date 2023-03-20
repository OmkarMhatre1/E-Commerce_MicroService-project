package PaymentService.service;

import PaymentService.model.PaymentRequest;
import PaymentService.model.PaymentResponse;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);


    PaymentResponse getPaymentDetailsByOrderId(String orderId);
}
