package com.revature.springcloud.order_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.springcloud.order_service.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.order-events:order-events}")
    private String topic;

    public void publishOrderPlaced(OrderPlacedEvent event) {

        try {

            String key = String.valueOf(event.getProductId());

            String json = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(topic, key, json);

            log.info("OrderPlacedEvent sent: {}", json);

        } catch (Exception e) {
            log.error("Failed to serialize event {}", e.getMessage());
        }
    }
}