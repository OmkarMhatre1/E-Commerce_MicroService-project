package OrderService.Service;

import OrderService.Model.OrderRequest;
import OrderService.Model.OrderResponse;

public interface OrderService {


    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
