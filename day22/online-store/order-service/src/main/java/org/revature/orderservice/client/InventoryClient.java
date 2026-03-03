package org.revature.orderservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;   // ← fixed import
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Service
@Slf4j
public class InventoryClient {

    private final RestTemplate restTemplate;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    public InventoryClient(RestTemplateBuilder builder) {    // ← fixed typo
        this.restTemplate = builder
                .connectTimeout(Duration.ofSeconds(3))
                .readTimeout(Duration.ofSeconds(3))
                .build();
    }

    /**
     * Ask Inventory Service: "Is this SKU available in this Quantity
     * Returns true if yes and the stock has been reserved.
     * Returns false if out of stock
     */
    public boolean checkAndReserve(String skuCode, int quantity) {

        String url = inventoryServiceUrl + "/api/v1/inventory/check"
                + "?skuCode=" + skuCode + "&quantity=" + quantity;
        log.info("Calling Inventory Service: {}", url);

        try{
            Boolean result = restTemplate.getForObject(url, Boolean.class);
            return Boolean.TRUE.equals(result);
        }catch (Exception e){
            log.error("Inventory Service called failed: {}", e.getMessage());
            throw new RuntimeException(
                    "Cannot reach Inventory Service, please try again later"
            );
        }
    }
}