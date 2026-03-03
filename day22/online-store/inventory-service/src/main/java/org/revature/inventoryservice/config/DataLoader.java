package org.revature.inventoryservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.revature.inventoryservice.model.InventoryItem;
import org.revature.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final InventoryRepository inventoryRepository;
    @Override
    public void run(String... args) throws Exception {

        //Add some products to inventory on startup
        inventoryRepository.saveAll(List.of(
                InventoryItem.builder()
                        .skuCode("IPHONE-15-BLACK")
                        .quantity(50)
                        .build(),
                InventoryItem.builder()
                        .skuCode("MACBOOK-PRO-M#")
                        .quantity(10)
                        .build(),
                InventoryItem.builder()
                        .skuCode("AIRPODS-PRO")
                        .quantity(100)
                        .build(),
                InventoryItem.builder()
                        .skuCode("IPAD-AIR-WIFI")
                        .quantity(0)
                        .build()  //out of stock
        ));
        log.info("Inventory data loaded - 4 products ready");

    }
}
