package org.example;

import java.util.concurrent.atomic.AtomicBoolean;

public class App{
public static void processOrder(Order order) throws InterruptedException {

    System.out.println("\n╔══════════════════════════════════════════════╗");
    System.out.println("  ║  NEW ORDER RECEIVED: " + order.getOrderId()+"║");
    System.out.println("  ╚══════════════════════════════════════════════╝");
    System.out.println("Customer: " + order.getCustomerId() +
            " | Product: " + order.getProductId() +
            " | Qty: " + order.getQuantity() +
            " | Amount: $" + order.getTotalAmount());
    System.out.println("------------------------------------------------");

    long orderStartTime = System.currentTimeMillis();

    // Thread-safe result flags
    AtomicBoolean paymentApproved = new AtomicBoolean(false);
    AtomicBoolean stockAvailable = new AtomicBoolean(false);

    // --- PHASE 1: Run Payment & Inventory checks CONCURRENTLY ---

    Thread paymentThread = new Thread(
            new PaymentValidationTask(order, paymentApproved),
            "Payment-Thread"
    );

    Thread inventoryThread = new Thread(
            new InventoryCheckTask(order, stockAvailable),
            "Inventory-Thread"
    );

    // Start both threads simultaneously
    paymentThread.start();
    inventoryThread.start();

    System.out.println("[Main-Thread] ⏳ Waiting for payment & inventory checks...");

    // Wait for BOTH to complete before proceeding
    paymentThread.join(); // Wait for payment thread
    inventoryThread.join(); // Wait for inventory thread

    System.out.println("[Main-Thread] Both checks completed!");

    // --- PHASE 2: Decision Gate ---

    if (paymentApproved.get() && stockAvailable.get()) {

        System.out.println("[Main-Thread] 🟢 All checks PASSED! Proceeding with order...");

        // --- PHASE 3: Process Shipment (main thread) ---
        ShipmentProcessor shipper = new ShipmentProcessor();
        shipper.processShipment(order);

        // --- PHASE 4: Send Email (independent thread — fire and forget) ---
        // We DON'T join this thread — email can be sent in background
        // while the system moves on to the next order
        Thread emailThread = new Thread(
                new EmailNotificationTask(order, "CONFIRMED & SHIPPED"),
                "Email-Thread"
        );
        emailThread.setDaemon(true); // Daemon: won't prevent JVM shutdown
        emailThread.start();
        // No join() here — fire and forget!

        System.out.println("[Main-Thread] ✅ ORDER " + order.getOrderId() + " SUCCESSFULLY PROCESSED!");

    } else {

        String reason = !paymentApproved.get() ? "Payment Failed" : "Insufficient Stock";
        System.out.println("[Main-Thread] 🔴 Order REJECTED — Reason: " + reason);

        // Notify customer of rejection
        Thread emailThread = new Thread(
                new EmailNotificationTask(order, "REJECTED — " + reason),
                "Rejection-Email-Thread"
        );
        emailThread.start();
        emailThread.join(); // Wait for rejection email (customer needs to know immediately)
    }

    long totalTime = System.currentTimeMillis() - orderStartTime;
    System.out.println("[Main-Thread] ⏱️  Order processing time: " + totalTime + "ms");
    System.out.println("================================================\n");
}

public static void main(String[] args) throws InterruptedException {

    System.out.println("🛒 E-COMMERCE ORDER PROCESSING SYSTEM STARTED");
    System.out.println("Main Thread: " + Thread.currentThread().getName());
    System.out.println("================================================");

    // Process multiple orders concurrently (in real system, these come from HTTP requests)
    Order order1 = new Order("ORD-001", "CUST-101", "LAPTOP-PRO", 2, 2999.98);
    Order order2 = new Order("ORD-002", "CUST-102", "PHONE-MAX", 15, 14999.85); // Stock will fail
    Order order3 = new Order("ORD-003", "CUST-103", "TABLET-AIR", 1, 799.99);

    // Process all 3 orders concurrently (each in its own orchestration thread)
    Thread orderThread1 = new Thread(() -> {
        try { processOrder(order1); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }, "OrderOrchestrator-1");

    Thread orderThread2 = new Thread(() -> {
        try { processOrder(order2); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }, "OrderOrchestrator-2");

    Thread orderThread3 = new Thread(() -> {
        try { processOrder(order3); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }, "OrderOrchestrator-3");

    orderThread1.start();
    orderThread2.start();
    orderThread3.start();

    orderThread1.join();
    orderThread2.join();
    orderThread3.join();

    System.out.println("🏁 ALL ORDERS PROCESSED. System ready for next batch.");
}
}