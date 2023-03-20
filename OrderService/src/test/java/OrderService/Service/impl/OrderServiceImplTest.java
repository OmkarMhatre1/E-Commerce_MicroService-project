package OrderService.Service.impl;

import OrderService.Entity.Order;
import OrderService.Exception.CustomException;
import OrderService.External.client.PaymentService;
import OrderService.External.client.ProductService;
import OrderService.External.request.PaymentRequest;
import OrderService.Model.*;
import OrderService.Repository.OrderRepository;
import OrderService.Service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductService productService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    OrderService orderService = new OrderServiceImpl();

    @DisplayName("Get order success Scenario")
    @Test
    void test_when_Order_Success() {
        //Mocking

        Order order = getMockOrder();

        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));

        when(restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/" + order.getProductId(), ProductResponse.class
        )).thenReturn(getMockProductResponse());
        when(restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/" + order.getId(), PaymentResponse.class))
                .thenReturn(getMockPaymentResponse());
        //Actual

        OrderResponse orderResponse = orderService.getOrderDetails(1);
        //Verification

        verify(orderRepository, times(1)).findById(anyLong());
        verify(restTemplate, times(1)).getForObject(
                "http://PRODUCT-SERVICE/product/" + order.getProductId(), ProductResponse.class
        );
        verify(restTemplate, times(1)).getForObject(
                "http://PAYMENT-SERVICE/payment/order/" + order.getId(), PaymentResponse.class
        );
        //Assert

        assertNotNull(orderResponse);
        assertEquals(order.getId(),orderResponse.getOrderId());
    }
    @DisplayName("Test Case When order not found")
    @Test
    void test_case_when_order_not_found(){


        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null));

        //OrderResponse orderResponse = orderService.getOrderDetails(1);
        //Assertion

        CustomException exception = assertThrows(CustomException.class,
                ()-> orderService.getOrderDetails(1));

        assertEquals("NOT_FOUND",exception.getErrorCode());
        assertEquals(404,exception.getStatus());

        //verify
        verify(orderRepository, times(1))
                .findById(anyLong());

    }

    @DisplayName("Place order success scenario")
    @Test
    void test_when_place_order_success(){
        //Mocking
        Order order = getMockOrder();
        OrderRequest orderRequest=getMockOrderRequest();

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);
        when(productService.reduceQuantity(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenReturn(new ResponseEntity<Long>(1L,HttpStatus.OK));
        //Actual
        long orderId=orderService.placeOrder(orderRequest);
        //Assertion
        assertEquals(order.getId(),orderId);
        //Verify
        verify(orderRepository, times(2))
                .save(any());
        verify(productService, times(1))
                .reduceQuantity(anyLong(), anyLong());
        verify(paymentService, times(1))
                .doPayment(any(PaymentRequest.class));

    }

    @DisplayName("Order Fail Scenario")
    @Test
    void testWhenOrderFail(){
        //Mocking
        Order order = getMockOrder();
        OrderRequest orderRequest=getMockOrderRequest();

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);
        when(productService.reduceQuantity(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenThrow(new RuntimeException());
        //Actual
        long orderId=orderService.placeOrder(orderRequest);
        //Assertion
        assertEquals(order.getId(),orderId);
        //Verify
        verify(orderRepository, times(2))
                .save(any());
        verify(productService, times(1))
                .reduceQuantity(anyLong(), anyLong());
        verify(paymentService, times(1))
                .doPayment(any(PaymentRequest.class));
        assertEquals(order.getId(),orderId);

    }

    private OrderRequest getMockOrderRequest() {
        return OrderRequest.builder()
                .productId(2)
                .totalAmount(100)
                .quantity(10)
                .paymentMode(PaymentMode.CASH)
                .build();
    }


    private PaymentResponse getMockPaymentResponse() {
        PaymentResponse paymentResponse = PaymentResponse.builder()
                .paymentId(1)
                .status("ACCEPTED")
                .paymentMode(PaymentMode.CASH)
                .amount(2000)
                .paymentDate(Instant.now())
                .orderId(1)
                .build();
        return paymentResponse;
    }

    private ProductResponse getMockProductResponse() {
        return ProductResponse.builder()
                .productId(2)
                .productName("OnePlus")
                .price(5000)
                .quantity(200)
                .build();
    }

    private Order getMockOrder() {
        Order order = Order.builder()
                .id(1)
                .orderStatus("PLACED")
                .amount(200)
                .orderDate(Instant.now())
                .quantity(200)
                .productId(2)
                .build();
        return order;
    }


}