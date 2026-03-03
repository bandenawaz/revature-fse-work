package org.revature.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.revature.orderservice.client.InventoryClient;
import org.revature.orderservice.dto.CreateOrderRequest;
import org.revature.orderservice.model.Order;
import org.revature.orderservice.model.OrderStatus;
import org.revature.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public Order placeOrder(CreateOrderRequest request){
        log.info("***** New Order Request *****");
        log.info("Customer: {}  | SKU {}  | QTY: {}",
                request.getCustomerEmail(), request.getSkuCode(), request.getQuantity());

        //---- Step 1: Check inventory(synchronous rest call ------
        log.info("STEP 1: Checking inventory");

        boolean inStock = inventoryClient.checkAndReserve(
                request.getSkuCode(), request.getQuantity()
        );

        if (!inStock) {
            //Save the order so the customer can see it in their history
            Order failedOrder = orderRepository.save(Order.builder()
                            .orderNumber("ORD-"+System.currentTimeMillis())
                            .skuCode(request.getSkuCode())
                            .quantity(request.getQuantity())
                            .price(request.getPrice())
                            .customerEmail(request.getCustomerEmail())
                            .status(OrderStatus.OUT_OF_STOCK)
                            .createdAt(LocalDateTime.now())
                    .build());

            log.warn("Order {} rejected - OUT OF STOCK", failedOrder.getOrderNumber());
            return failedOrder;
        }

        // Step 2: Save the order to our database
        log.info("Step 2: Inventory Confirmed. Saving order..");

        Order order = orderRepository.save(Order.builder()
                        .orderNumber("ORD-"+System.currentTimeMillis())
                        .skuCode(request.getSkuCode())
                        .quantity(request.getQuantity())
                        .price(request.getPrice())
                        .customerEmail(request.getCustomerEmail())
                        .status(OrderStatus.PLACED)
                        .createdAt(LocalDateTime.now())
                .build());
        log.info("Order saved {}", order.getOrderNumber());

        return order;
    }
}
