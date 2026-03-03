package org.revature.inventoryservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.revature.inventoryservice.model.InventoryItem;
import org.revature.inventoryservice.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * Order Service calls this endpoint
     * Returns true if item is in stock and has been reserved
     * Returns false if out of stock
     *
     * Example: GET /api/v1/inventory/check?skuCode=IPHONE-15-BLACK&quantity=1
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkInventory(
            @RequestParam String skuCode,
            @RequestParam int quantity
    ) {
        log.info("Inventory check request - SKU: {} | QTY: {}", skuCode, quantity);
        boolean isAvailable = inventoryService.checkAndReserve(skuCode, quantity);
        return ResponseEntity.ok(isAvailable);
    }

}
