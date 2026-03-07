package com.revature.springcloud.order_service.client;

import com.revature.springcloud.order_service.dto.ProductResponse;
import com.revature.springcloud.order_service.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductClientFallbackFactory implements FallbackFactory<ProductClient> {

    @Override
    public ProductClient create(Throwable cause) {
        log.error("ProductClient fallback triggered. Cause: {}", cause.getMessage());

        return new ProductClient() {
            @Override
            public ProductResponse getProductById(Long id) {

                //Check: is this a business error (product truly not found)?
                if (cause.getMessage() != null && cause.getMessage().contains("404")){
                    //Don't hide 404s - propagate as business exception
                    //This does not open the circuit breaker (configured in ignore-exceptions
                    throw new ProductNotFoundException(id);
                }

                //Product Service is DOWN - return a safe degraded response
                log.warn("Product Service is Down, Returning unavailable response fpr product {}", id);

                ProductResponse unavailable = new ProductResponse();
                unavailable.setId(id);
                unavailable.setName("UNAVAILABLE");
                unavailable.setPrice(0.0);
                unavailable.setStock(0);
                return unavailable;
            }
        };
    }
}
