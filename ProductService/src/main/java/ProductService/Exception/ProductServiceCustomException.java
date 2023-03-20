package ProductService.Exception;

public class ProductServiceCustomException extends RuntimeException {
    public ProductServiceCustomException(String message) {
        super(message);
    }
}
