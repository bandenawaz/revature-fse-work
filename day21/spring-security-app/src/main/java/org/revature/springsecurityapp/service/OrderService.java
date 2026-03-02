package org.revature.springsecurityapp.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/*
This class handles method security
 */
@Service
public class OrderService {
    /*
    Any authenticated user can view an order
    //No annotations needed - the URL rule already requires authentication
     */
    public String getOrder(Long orderId) {
        return "Order details for order #" + orderId;
    }

    //Only Manager or Admin can approve orders
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public String approveOrder(Long orderId) {
        return "Order #"+orderId+ " has been approved.";
    }

    //Only Admin can delete Orders
    @PreAuthorize("hasRole('ADMIN')")
    public String deleterOrder(Long orderId) {
        return "Order #"+orderId+ " has been deleted.";
    }

    //A User can only cancel their order
    //#requestedBy is the method parameter name - SpEL reads it directly
    @PreAuthorize("#requestedBy == authentication.principal.username or hasRole('ADMIN')")
    public String cancelOrder(Long orderId, String requestedBy) {
        return "Order #"+orderId+ " has been cancelled by"+requestedBy;
    }
}
