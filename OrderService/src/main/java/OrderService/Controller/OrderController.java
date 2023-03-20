package OrderService.Controller;

import OrderService.Model.OrderRequest;
import OrderService.Model.OrderResponse;
import OrderService.Service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
@Slf4j
public class OrderController {

    private final OrderService orderService;

   // @PreAuthorize("hasAuthority('Customer')")
    @PostMapping
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest){
        long orderId = orderService.placeOrder(orderRequest);
        log.info("Order id:{}",orderId);
        return new ResponseEntity<>(orderId,HttpStatus.OK);

    }

   // @PreAuthorize("hasAuthority('Admin') || hasAuthority('Customer')")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse>  getOrderDetails(@PathVariable long orderId){
        OrderResponse orderResponse=orderService.getOrderDetails(orderId);
        return new ResponseEntity<>(orderResponse,HttpStatus.FOUND);
    }

}
