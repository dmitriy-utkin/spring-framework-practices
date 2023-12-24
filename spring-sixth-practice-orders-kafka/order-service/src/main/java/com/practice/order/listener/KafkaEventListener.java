package com.practice.order.listener;

import com.practice.order.event.OrderEvent;
import com.practice.order.mapper.OrderMapper;
import com.practice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaEventListener {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @KafkaListener(
            topics = "${app.kafka.statusTopic}",
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
        orderService.updateStatus(orderMapper.orderEventToOrder(orderEvent));
    }
}
