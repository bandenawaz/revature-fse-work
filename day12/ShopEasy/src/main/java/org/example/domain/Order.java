package org.example.domain;

import java.time.LocalDate;
import java.util.List;

public class Order {

    public String orderId;
    String customerId;
    public String customerName;
    public String orderStatus; // PENDING, SHIPPED, DELIVERED
    public List<OrderItem> orderItems;
    LocalDate orderDate;
    public boolean expressShipping;

    public Order(String orderId, String customerId, String customerName,
                 List<OrderItem> orderItems, // ← Position 4 (swapped)
                 LocalDate orderDate,        // ← Position 5 (swapped)
                 String orderStatus,         // ← Position 6 (moved)
                 boolean expressShipping) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.orderStatus = orderStatus;
        this.orderItems = orderItems;
        this.orderDate = orderDate;
        this.expressShipping = expressShipping;
    }

    //calculate total amount
    public double getTotalAmount() {
        return orderItems.stream()
                .mapToDouble(item -> item.price * item.quantity)
                .sum();
    }

    //to String method


    @Override
    public String toString() {
        return "Order "+orderId+ " - "+customerName + " - $"+
                String.format("%.2f", getTotalAmount());
    }
}
