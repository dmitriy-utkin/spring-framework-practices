package com.practice.order.repository;

import com.practice.order.model.Order;
import lombok.Builder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Builder
public class OrderRepository {

    @Builder.Default
    private Map<String, Order> orders = new HashMap<>();

    public List<Order> findAll() {
        return orders.values().stream().toList();
    }

    public Order save(Order order) {
        orders.put(getKey(order), order);
        return order;
    }

    public Order updateStatus(Order order) {
        String key = getKey(order);
        Order existedOrder = orders.get(key);
        existedOrder.setStatus(order.getStatus());
        orders.put(key, existedOrder);
        return existedOrder;
    }

    private String getKey(Order order) {
        return order.getProduct() + order.getQuantity();
    }

}
