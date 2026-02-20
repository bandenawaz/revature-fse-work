package org.example;
// ============================================
// TASK 3: Email Notification Thread
// ============================================

class EmailNotificationTask implements Runnable {
    
    private Order order;
    private String status;
    
    public EmailNotificationTask(Order order, String status) {
        this.order = order;
        this.status = status;
    }
    
    @Override
    public void run() {
        System.out.println("[" + Thread.currentThread().getName() + "] " +
                           "📧 Preparing email notification for Customer: " + order.getCustomerId());
        
        try {
            // Simulate SMTP call (1.5 seconds)
            Thread.sleep(1500);
            
            System.out.println("[" + Thread.currentThread().getName() + "] " +
                               "📧 Email SENT to Customer " + order.getCustomerId() + 
                               " — Order " + order.getOrderId() + " is: " + status);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
