package com.revature.springcloud.order_service.service;

import com.revature.springcloud.order_service.client.ProductClient;
import com.revature.springcloud.order_service.dto.ProductResponse;
import com.revature.springcloud.order_service.entity.Order;
import com.revature.springcloud.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;  //<- Feign replaces Rest Template

    public Order createOrder(Long productId, Integer quantity){

       ProductResponse productResponse = productClient.getProductById(productId);
        if (productResponse == null){
            throw new RuntimeException("Product not found with id: "+productId);
        }

        if (productResponse.getStock() < quantity){
            throw new RuntimeException("Insufficient stock "+productResponse.getStock());
        }

        Order order = Order.builder()
                .productId(productId)
                .quantity(quantity)
                .totalAmount(productResponse.getPrice() * quantity)
                .status("CONFIRMED")
                .build();

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id){
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found :"+id));
    }

    public Order updateOrder(Long id, String status){
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id){
        orderRepository.deleteById(id);
    }
}
