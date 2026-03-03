package org.revature.orderservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.revature.orderservice.dto.CreateOrderRequest;
import org.revature.orderservice.model.Order;
import org.revature.orderservice.model.OrderStatus;
import org.revature.orderservice.repository.OrderRepository;
import org.revature.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Order> placeOrder(@RequestBody CreateOrderRequest request) {
        Order order = orderService.placeOrder(request);

        if ((order.getStatus() == OrderStatus.OUT_OF_STOCK)) {

            // Return 409 Conflict for out-of-stock
            return ResponseEntity.status(HttpStatus.CONFLICT).body(order);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping
    public List<Order>  getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
