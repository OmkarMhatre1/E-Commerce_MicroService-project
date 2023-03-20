package OrderService.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {

    private long orderId;
    private long productId;
    private long totalAmount;
    private long quantity;
    private PaymentMode paymentMode;



}
