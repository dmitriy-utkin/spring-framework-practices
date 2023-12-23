package com.practice.order.mapper;

import com.practice.order.model.Order;
import com.practice.order.web.model.OrderResponse;
import com.practice.order.web.model.UpsertOrder;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order requestToOrder(UpsertOrder request) {
        return Order.builder()
                .product(request.getProduct())
                .quantity(request.getQuantity())
                .build();
    }

    public OrderResponse orderToResponse(Order order) {
        return OrderResponse.builder()
                .product(order.getProduct())
                .quantity(order.getQuantity())
                .build();
    }
}
