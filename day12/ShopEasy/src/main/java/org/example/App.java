package org.example;

import org.example.data.ECommerceAnalytics;
import org.example.domain.Order;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.data.ECommerceAnalytics.generateOrders;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {

        List<Order> orders = ECommerceAnalytics.generateOrders();

        System.out.println("====================================================");
        System.out.println("       E-COMMERCE ANALYTICS DASHBOARD              ");
        System.out.println("====================================================\n");

        // ============================================
        // 1. HIGH-VALUE ORDERS (> $500)
        // ============================================
        System.out.println("1️⃣  HIGH-VALUE ORDERS (> $500):");
        System.out.println("─────────────────────────────────");

        orders.stream()
                .filter(o -> o.getTotalAmount() > 500)
                .sorted((o1, o2) -> Double.compare(
                        o2.getTotalAmount(), o1.getTotalAmount()))
                .forEach(System.out::println);

        System.out.println();

        // ============================================
        // 2. TOTAL REVENUE
        // ============================================
        System.out.println("2️⃣  TOTAL REVENUE:");
        System.out.println("─────────────────────────────────");

        double totalRevenue = orders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();

        System.out.println("Total Revenue: $" + String.format("%.2f", totalRevenue));
        System.out.println();

        // ============================================
        // 3. REVENUE BY CATEGORY
        // ============================================
        System.out.println("3️⃣  REVENUE BY CATEGORY:");
        System.out.println("─────────────────────────────────");

        Map<String, Double> revenueByCategory = orders.stream()
                        .flatMap(order -> order.orderItems.stream())
                                .collect(Collectors.groupingBy(
                                        item -> item.category,
                                        Collectors.summingDouble(item -> item.price * item.quantity)
                                ));

        revenueByCategory.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .forEach(e -> System.out.println(
                        e.getKey() + ": $" + String.format("%.2f", e.getValue())));

        System.out.println();

        // ============================================
        // 4. EXPRESS SHIPPING ORDERS
        // ============================================
        System.out.println("4️⃣  EXPRESS SHIPPING ORDERS:");
        System.out.println("─────────────────────────────────");

        List<Order> expressOrders = orders.stream()
                .filter(o -> o.expressShipping)
                .collect(Collectors.toList());

        System.out.println("Count: " + expressOrders.size());
        expressOrders.forEach(o ->
                System.out.println("  → " + o.orderId + " (" + o.customerName + ")")
        );
        System.out.println();

        // ============================================
        // 5. TOP 3 CUSTOMERS BY SPENDING
        // ============================================
        System.out.println("5️⃣  TOP 3 CUSTOMERS BY SPENDING:");
        System.out.println("─────────────────────────────────");

        orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.customerName,
                        Collectors.summingDouble(Order::getTotalAmount)
                ))
                .entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(3)
                .forEach(e -> System.out.println(
                        e.getKey() + ": $" + String.format("%.2f", e.getValue())));

        System.out.println();

        // ============================================
        // 6. AVERAGE ORDER VALUE
        // ============================================
        System.out.println("6️⃣  AVERAGE ORDER VALUE:");
        System.out.println("─────────────────────────────────");

        double avg = orders.stream()
                .mapToDouble(Order::getTotalAmount)
                .average()
                .orElse(0.0);

        System.out.println("Average: $" + String.format("%.2f", avg));
        System.out.println();

        // ============================================
        // 7. ORDERS BY STATUS
        // ============================================
        System.out.println("7️⃣  ORDERS BY STATUS:");
        System.out.println("─────────────────────────────────");

        orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.orderStatus,
                        Collectors.counting()
                ))
                .forEach((status, count) ->
                        System.out.println(status + ": " + count + " orders"));

        System.out.println();

        // ============================================
        // 8. MOST POPULAR CATEGORY
        // ============================================
        System.out.println("8️⃣  MOST POPULAR CATEGORY:");
        System.out.println("─────────────────────────────────");

        Map<String, Long> itemCountByCategory = orders.stream()
                .flatMap(o -> o.orderItems.stream())
                .collect(Collectors.groupingBy(
                        item -> item.category,
                        Collectors.counting()
                ));

        itemCountByCategory.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(e -> System.out.println(
                        "Most Popular: " + e.getKey()
                                + " (" + e.getValue() + " items sold)"));

        System.out.println("\n====================================================");
    }
}