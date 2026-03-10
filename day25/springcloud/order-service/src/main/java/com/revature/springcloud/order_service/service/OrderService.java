package com.revature.springcloud.order_service.service;

import com.revature.springcloud.order_service.client.ProductClient;
import com.revature.springcloud.order_service.dto.ProductResponse;
import com.revature.springcloud.order_service.entity.Order;
import com.revature.springcloud.order_service.event.OrderPlacedEvent;
import com.revature.springcloud.order_service.kafka.OrderEventProducer;
import com.revature.springcloud.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductClient productClient;  //<- Feign replaces Rest Template
    private final OrderEventProducer eventProducer;

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

        //Publish event to Kafka - AFTER the order is saved
        //This is fore-and-foreget from order-service's perspective
        //Product service will consume this and update stock asynchronously
        eventProducer.publishOrderPlaced(OrderPlacedEvent.builder()
                .orderId(order.getId())
                .productId(productId)
                .quantity(quantity)
                .status("PLACED")
                .build());
        log.info("Order {} created. Stock update event published to Kafka.",
                order.getId());

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

    //Publish a cancellation event  - product service will restore stock
    public void deleteOrder(Long id){
        Order order = getOrderById(id);
        orderRepository.deleteById(id);
        eventProducer.publishOrderPlaced(OrderPlacedEvent.builder()
                .orderId(order.getId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .status("CANCELLED")
                .build());
        log.info("Order {} cancelled. Stock update event published to Kafka.",
                order.getId());


    }
}
