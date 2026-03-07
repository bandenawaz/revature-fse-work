package com.revature.springcloud.order_service.exception;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(Long id){
        super("Product Not found: "+id);
    }
}
