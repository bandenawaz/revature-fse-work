package org.example;
// ============================================
// TASK 1: Payment Validation Thread
// ============================================

import java.util.concurrent.atomic.AtomicBoolean;

class PaymentValidationTask implements Runnable {
    
    private Order order;
    private AtomicBoolean paymentApproved; // Thread-safe flag
    
    public PaymentValidationTask(Order order, AtomicBoolean paymentApproved) {
        this.order = order;
        this.paymentApproved = paymentApproved;
    }
    
    @Override
    public void run() {
        System.out.println("[" + Thread.currentThread().getName() + "] " +
                           "🔐 Starting payment validation for Order: " + order.getOrderId());
        
        try {
            // Simulate calling Payment Gateway API (2 seconds)
            System.out.println("[" + Thread.currentThread().getName() + "] " +
                               "Contacting payment gateway...");
            Thread.sleep(2000);
            
            // Simulate payment approval (in real system, check gateway response)
            boolean approved = order.getTotalAmount() < 50000; // Reject large orders for demo
            paymentApproved.set(approved);
            
            if (approved) {
                System.out.println("[" + Thread.currentThread().getName() + "] " +
                                   "✅ Payment APPROVED for $" + order.getTotalAmount());
            } else {
                System.out.println("[" + Thread.currentThread().getName() + "] " +
                                   "❌ Payment REJECTED for $" + order.getTotalAmount());
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[" + Thread.currentThread().getName() + "] " +
                               "Payment validation interrupted!");
        }
    }
}
