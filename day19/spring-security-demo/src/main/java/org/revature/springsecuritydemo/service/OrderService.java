package org.revature.springsecuritydemo.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    // Any authenticated user can view an order
    // No annotation needed — the URL rule already requires authentication
    public String getOrder(Long orderId) {
        return "Order details for order #" + orderId;
    }

    // Only MANAGER or ADMIN can approve orders
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public String approveOrder(Long orderId) {
        return "Order #" + orderId + " has been approved.";
    }

    // Only ADMIN can delete orders
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteOrder(Long orderId) {
        return "Order #" + orderId + " has been deleted.";
    }

    // A user can only cancel their own order
    // #requestedBy is the method parameter name — SpEL reads it directly
    @PreAuthorize("#requestedBy == authentication.principal.username or hasRole('ADMIN')")
    public String cancelOrder(Long orderId, String requestedBy) {
        return "Order #" + orderId + " cancelled by " + requestedBy;
    }

    @PreAuthorize("hasRole('AUDITOR') or hasRole('ADMIN')" )
    public String generateReport(String month){

        return "Report for " + month;
    }

    @PreAuthorize("#requested")
    public String updateProfile(String username, String newEmail){
        return "Profile for " + username + " has been updated with new "+newEmail+".";
    }
}