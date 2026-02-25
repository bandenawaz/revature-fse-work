package org.revature.productapp.repository;

import org.revature.productapp.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {
    private List<Product> products = new ArrayList<>();

    //lets create products
    public void createProduct() {
        products = List.of(
                new Product(1, "Laptop", 1200.00, 10),
        new Product(2, "Smartphone", 800.00, 25),
        new Product(3, "Tablet", 450.00, 15),
        new Product(4, "Headphones", 150.00, 40),
        new Product(5, "Keyboard", 70.00, 35),
        new Product(6, "Mouse", 40.00, 50),
        new Product(7, "Monitor", 300.00, 20),
        new Product(8, "Printer", 250.00, 12),
        new Product(9, "Webcam", 90.00, 30),
        new Product(10, "Speaker", 110.00, 28),
        new Product(11, "External HDD", 130.00, 18),
        new Product(12, "USB Drive", 25.00, 100),
        new Product(13, "Router", 95.00, 22),
        new Product(14, "Microphone", 180.00, 16),
        new Product(15, "Smartwatch", 220.00, 14)
        );
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public Product getProductById(int id) {
        for(int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductId() == id) {
                return products.get(i);
            }
        }
        return null;
    }

    public Product saveProduct(Product product) {

        Product savedProduct = new Product();
        savedProduct.setProductId(product.getProductId());
        savedProduct.setProductName(product.getProductName());
        savedProduct.setProductPrice(product.getProductPrice());
        savedProduct.setProductQuantity(product.getProductQuantity());
        products.add(savedProduct);
        return savedProduct;
    }

    //Delete
    //Update

}
