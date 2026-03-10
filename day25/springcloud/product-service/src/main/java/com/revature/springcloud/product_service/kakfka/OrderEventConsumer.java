package com.revature.springcloud.product_service.kakfka;

import com.revature.springcloud.product_service.entity.Product;
import com.revature.springcloud.product_service.event.OrderPlacedEvent;
import com.revature.springcloud.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEventConsumer {
    private final ProductRepository productRepository;

    @KafkaListener(
            topics = "${kafka.topic.order-events:order-events}",
            groupId = "product-service-group"
    )
    @Transactional
    public void handleOrderEvent(
            @Payload OrderPlacedEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset){
        log.info("<- Received OrderEvent from Kafka: partion={}, offset={}, event={} ",
                partition, offset, event);
        Product product = productRepository.findById(event.getProductId())
                .orElseThrow(() ->{
                    log.error("KAFKA: Pproduct not found: {}. Cannot process event: {}",
                            event.getProductId(), event);
                    return new RuntimeException("Product not found "+event.getProductId());
                });

        if("PLACED".equals(event.getStatus())){
            //VALIDATE sufficient stock before deducting
            if(product.getStock() < event.getQuantity()){
                log.error("KAFKA: Insufficient Stock! Product={}, available={}, required={}."+
                        "Order {} may be inconsistent.",
                        product.getId(), product.getStock(), event.getQuantity(), event.getOrderId());
                //In production: publish a compensation event to order-service
                // to trigger order cancellation (SAGA pattern)
                return;
            }
            product.setStock(product.getStock() - event.getQuantity());
            productRepository.save(product);
            log.info("Stock reduced: product={}, new stock={}",
                    product.getId(), product.getStock());
        }
        else if ("CANCELLED".equals(event.getStatus())){
            //Order cancelled - restore stock
            product.setStock((product.getStock() + event.getQuantity()));
            productRepository.save(product);
            log.info("stock restored: product={}, new stock={}", product.getId(), product.getStock());
        }

    }
}
