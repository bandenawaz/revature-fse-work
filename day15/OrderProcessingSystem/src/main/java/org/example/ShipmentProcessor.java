package org.example;
// ============================================
// TASK 4: Shipment Processing (Main Thread)
// ============================================

class ShipmentProcessor {
    
    public void processShipment(Order order) {
        System.out.println("[" + Thread.currentThread().getName() + "] " +
                           "🚚 Processing shipment for Order: " + order.getOrderId());
        
        try {
            Thread.sleep(300); // Simulate shipment creation
            System.out.println("[" + Thread.currentThread().getName() + "] " +
                               "🚚 Shipment CREATED! Tracking ID: TRK-" + 
                               System.currentTimeMillis() + " for Order: " + order.getOrderId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
