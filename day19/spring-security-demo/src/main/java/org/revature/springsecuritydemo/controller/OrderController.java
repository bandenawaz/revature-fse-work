package org.revature.springsecuritydemo.controller;

import org.revature.springsecuritydemo.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @PostMapping("/{id}/approve")
    public String approveOrder(@PathVariable Long id) {
        return orderService.approveOrder(id);
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, @RequestParam String requestedBy) {
        return orderService.cancelOrder(id, requestedBy);
    }
}