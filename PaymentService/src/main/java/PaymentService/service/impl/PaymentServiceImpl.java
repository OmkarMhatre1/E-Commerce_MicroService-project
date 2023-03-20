package PaymentService.service.impl;

import PaymentService.entity.TransactionDetails;
import PaymentService.model.PaymentMode;
import PaymentService.model.PaymentRequest;
import PaymentService.model.PaymentResponse;
import PaymentService.repository.TransactionRepository;
import PaymentService.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentServiceImpl implements PaymentService {

    private final TransactionRepository transactionRepository;

    @Override
    public long doPayment(PaymentRequest paymentRequest) {
        log.info("Recording payment details:{}",paymentRequest);
        TransactionDetails transactionDetails= TransactionDetails.builder()
                .orderId(paymentRequest.getOrderId())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .paymentDate(Instant.now())
                .paymentStatus("SUCCESS")
                .amount(paymentRequest.getAmount())
                .build();

        transactionRepository.save(transactionDetails);

        log.info("Transaction completed with id: {}",transactionDetails.getId());

        return transactionDetails.getId();
    }

    @Override
    public PaymentResponse getPaymentDetailsByOrderId(String orderId) {
        log.info("Getting payment details for order Id: ",orderId);

        TransactionDetails transactionDetails=transactionRepository.findByOrderId(Long.valueOf(orderId));

        return PaymentResponse.builder()
                .paymentId(transactionDetails.getId())
                .paymentMode(PaymentMode.valueOf(transactionDetails.getPaymentMode()))
                .paymentDate(transactionDetails.getPaymentDate())
                .orderId(transactionDetails.getOrderId())
                .status(transactionDetails.getPaymentStatus())
                .amount(transactionDetails.getAmount())
                .build();
    }
}
