package com.practice.order.service;

import com.practice.order.event.OrderEvent;
import com.practice.order.mapper.OrderMapper;
import com.practice.order.model.Order;
import com.practice.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final OrderMapper orderMapper;

    @Value("${app.kafka.serviceTopic}")
    private String kafkaTopicName;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order save(Order order) {
        kafkaTemplate.send(kafkaTopicName, orderMapper.orderToOrderEvent(order));
        return orderRepository.save(order);
    }

    public Order updateStatus(Order order) {
        return orderRepository.updateStatus(order);
    }

}
