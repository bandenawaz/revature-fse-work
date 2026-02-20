package org.example;
// ============================================
// TASK 2: Inventory Check Thread
// ============================================

import java.util.concurrent.atomic.AtomicBoolean;

class InventoryCheckTask implements Runnable {
    
    private Order order;
    private AtomicBoolean stockAvailable; // Thread-safe flag
    
    public InventoryCheckTask(Order order, AtomicBoolean stockAvailable) {
        this.order = order;
        this.stockAvailable = stockAvailable;
    }
    
    @Override
    public void run() {
        System.out.println("[" + Thread.currentThread().getName() + "] " +
                           "📦 Checking inventory for Product: " + order.getProductId());
        
        try {
            // Simulate DB query (500ms — faster than payment)
            Thread.sleep(500);
            
            // Simulate stock check (assume stock available for qty <= 10)
            boolean hasStock = order.getQuantity() <= 10;
            stockAvailable.set(hasStock);
            
            if (hasStock) {
                System.out.println("[" + Thread.currentThread().getName() + "] " +
                                   "✅ Stock AVAILABLE — " + order.getQuantity() + 
                                   " units of " + order.getProductId());
            } else {
                System.out.println("[" + Thread.currentThread().getName() + "] " +
                                   "❌ INSUFFICIENT STOCK for " + order.getQuantity() + " units");
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
