package org.example.data;

import org.example.domain.Order;
import org.example.domain.OrderItem;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ECommerceAnalytics {

    public static List<Order> generateOrders() {
        return Arrays.asList(

                new Order("ORD001", "CUST001", "Alice Johnson",
                        Arrays.asList(
                                new OrderItem("Laptop", "Electronics", 1200.00, 1),
                                new OrderItem("Mouse",  "Electronics",   25.00, 2)
                        ),
                        LocalDate.of(2026, 2, 10), "DELIVERED", false
                ),

                new Order("ORD002", "CUST002", "Bob Smith",
                        Arrays.asList(
                                new OrderItem("Novel",    "Books",       15.00, 3),
                                new OrderItem("Notebook", "Stationery",   5.00, 5)
                        ),
                        LocalDate.of(2026, 2, 11), "SHIPPED", false
                ),

                new Order("ORD003", "CUST001", "Alice Johnson",
                        Arrays.asList(
                                new OrderItem("iPhone", "Electronics", 999.00, 1)
                        ),
                        LocalDate.of(2026, 2, 12), "PENDING", true
                ),

                new Order("ORD004", "CUST003", "Charlie Brown",
                        Arrays.asList(
                                new OrderItem("Headphones", "Electronics", 150.00, 1),
                                new OrderItem("Cable",       "Electronics",  20.00, 3)
                        ),
                        LocalDate.of(2026, 2, 13), "DELIVERED", false
                ),

                new Order("ORD005", "CUST002", "Bob Smith",
                        Arrays.asList(
                                new OrderItem("Desk",  "Furniture", 350.00, 1),
                                new OrderItem("Chair", "Furniture", 200.00, 1)
                        ),
                        LocalDate.of(2026, 2, 14), "PENDING", true
                ),

                new Order("ORD006", "CUST004", "Diana Prince",
                        Arrays.asList(
                                new OrderItem("Cookbook",      "Books",   25.00, 2),
                                new OrderItem("Kitchen Knife", "Kitchen", 45.00, 1)
                        ),
                        LocalDate.of(2026, 2, 15), "SHIPPED", false
                )
        );
    }
}