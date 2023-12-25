package com.practice.order.listener;

import com.practice.order.event.OrderEvent;
import com.practice.order.util.OrderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaEventListener {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Value("${app.kafka.statusTopic}")
    private String kafkaTopic;

    @KafkaListener(
            topics = "${app.kafka.serviceTopic}",
            groupId = "${app.kafka.kafkaGroupId}",
            containerFactory = "orderConcurrentKafkaListenerContainerFactory"
    )
    public void listen(@Payload OrderEvent orderEvent,
                       @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                       @Header(value = KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {
        log.info("Received new event: {}", orderEvent);
        log.info("Key - {}; Topic - {}; Partition - {}; Time - {}", key, topic, partition, timestamp);
        orderEvent.setStatus(OrderUtil.getRandomOrderStatus());
        kafkaTemplate.send(kafkaTopic, orderEvent);
    }
}
