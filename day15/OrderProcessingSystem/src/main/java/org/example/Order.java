package org.example;

import java.util.concurrent.atomic.AtomicBoolean;

// ============================================
// DOMAIN MODELS
// ============================================

class Order {
    private String orderId;
    private String customerId;
    private String productId;
    private int quantity;
    private double totalAmount;
    
    public Order(String orderId, String customerId, 
                String productId, int quantity, double totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }
    
    // Getters
    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public double getTotalAmount() { return totalAmount; }
}
