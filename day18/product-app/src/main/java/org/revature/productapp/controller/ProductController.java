package org.revature.productapp.controller;

import lombok.RequiredArgsConstructor;
import org.revature.productapp.model.Product;
import org.revature.productapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

//    @Autowired
//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("{id}")
    public Product getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.save(product);
    }
}
