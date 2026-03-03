package org.revature.inventoryservice.repository;

import org.revature.inventoryservice.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {

    // Pass 0 as minimumQuantity when calling this
    Optional<InventoryItem> findBySkuCodeAndQuantityGreaterThan(String skuCode,
                                                                int minimumQuantity);

    Optional<InventoryItem> findBySkuCode(String skuCode);
}
