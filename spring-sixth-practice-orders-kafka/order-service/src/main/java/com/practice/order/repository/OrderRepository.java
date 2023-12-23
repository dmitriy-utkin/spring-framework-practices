package com.practice.order.repository;

import com.practice.order.model.Order;
import lombok.Builder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Builder
public class OrderRepository {

    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    public Order save(Order order) {
        orders.add(order);
        return order;
    }

}
