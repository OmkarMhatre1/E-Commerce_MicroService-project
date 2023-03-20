package ProductService.Service;

import ProductService.Model.ProductRequest;
import ProductService.Model.ProductResponse;

public interface ProductService {


    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
