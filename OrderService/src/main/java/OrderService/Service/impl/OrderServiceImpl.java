package OrderService.Service.impl;

import OrderService.Entity.Order;
import OrderService.Exception.CustomException;
import OrderService.External.client.PaymentService;
import OrderService.External.client.ProductService;
import OrderService.External.request.PaymentRequest;
import OrderService.Model.OrderRequest;
import OrderService.Model.OrderResponse;
import OrderService.Model.PaymentResponse;
import OrderService.Model.ProductResponse;
import OrderService.Repository.OrderRepository;
import OrderService.Service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RestTemplate restTemplate;


    @Override
    public long placeOrder(OrderRequest orderRequest) {

        //Order Entity-> Save the data with status order created
        //Product Service-> Reduce quantity
        //Payment Service-> payment -> success -> or-else-> cancelled
        log.info("Placing Order Request :{}", orderRequest.getProductId());

        //feign
        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Crating order with status CREATED");

        Order order = Order.builder()
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .orderDate(Instant.now())
                .orderStatus("CREATED")
                .amount(orderRequest.getTotalAmount())
                .build();
        order = orderRepository.save(order);

        log.info("Calling payment service to complete the payment");

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();

//        String orderStatus = null;
//        if (orderStatus==null){
//
//            paymentService.doPayment(paymentRequest);
//            log.info("Payment done successfully");
//            orderStatus="PLACED";
//            }
//        else {
//            log.error("payment failed");
//            orderStatus="FAILED";
//        }
        String orderStatus = null;

        try {
            //feign
            paymentService.doPayment(paymentRequest);
            log.info("Payment done successfully");
            orderStatus = "PLACED";
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("Error in payment");
            orderStatus = "FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("Order place successfully with order id: {}", order.getId());
        return order.getId();

    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order detail for Order id : {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found"
                        , "NOT_FOUND", 404));

        log.info("Invoking product service to fetch the product");

        ProductResponse productResponse = restTemplate.getForObject("http://PRODUCT-SERVICE/product/" + order.getProductId(), ProductResponse.class);

        log.info("Getting payment information from the payment service");
        PaymentResponse paymentResponse = restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/" + order.getId(), PaymentResponse.class);

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .price(productResponse.getPrice())
                .quantity(productResponse.getQuantity())
                .build();

        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .status(paymentResponse.getStatus())
                .build();

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .quantity(order.getQuantity())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();

        return orderResponse;
    }
}

