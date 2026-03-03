package org.revature.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.revature.inventoryservice.model.InventoryItem;
import org.revature.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    /**
     * Check if the item is in stock.
     * If yes, reserve it by reducing the quantity
     *
     */
    public boolean checkAndReserve(String skuCode, int requestedQuantity) {

        log.info("Checking inventory for SKU: {}   |   Quantity Requested: {}",
                skuCode, requestedQuantity);

        Optional<InventoryItem> itemOptional = inventoryRepository.findBySkuCode(skuCode);

        if (itemOptional.isEmpty()){
            log.warn("SKU NOT FOUND IN INVENTORY: {}", skuCode);
            return false;
        }

        InventoryItem item = itemOptional.get();
        if (item.getQuantity() < requestedQuantity){
            log.warn("Insufficient quantity for SKU: {}  | Avaialble : {}  | Requested: {}",
                    skuCode, item.getQuantity(),requestedQuantity );
            return false;
        }

        //Reserver the Stock  - reduce the quantity
        item.setQuantity(item.getQuantity() - requestedQuantity);
        inventoryRepository.save(item);
        log.info("Reserved: {} units of {}. Remaining Stock: {}", requestedQuantity,
                skuCode, item.getQuantity());
        return true;
    }
}
