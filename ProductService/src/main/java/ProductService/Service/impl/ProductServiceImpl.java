package ProductService.Service.impl;

import ProductService.Entity.Product;
import ProductService.Exception.ProductQuantityException;
import ProductService.Exception.ProductServiceCustomException;
import ProductService.Model.ProductRequest;
import ProductService.Model.ProductResponse;
import ProductService.Repository.ProductRepository;
import ProductService.Service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;


    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding Product");

        Product product= Product.builder()
                .productName(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .build();

        repository.save(product);
        log.info("Product Created");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("Get the product for productId: {}",productId);

        Product product=repository.findById(productId)
                .orElseThrow(()-> new ProductServiceCustomException( "Product with given id not found"));

        ProductResponse productResponse=new ProductResponse();
        BeanUtils.copyProperties(product,productResponse);

        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduced Quantity {} for id: {}",productId,quantity);
        Product product=repository.findById(productId)
                .orElseThrow(()-> new ProductServiceCustomException(
                        "Product with give id not found"
                ));

        if (product.getQuantity()<quantity){
            throw new ProductQuantityException("Product does not have sufficient quantity");
        }

        product.setQuantity(product.getQuantity()-quantity);
        repository.save(product);
        log.info("Product quantity updated successfully");
    }
}
