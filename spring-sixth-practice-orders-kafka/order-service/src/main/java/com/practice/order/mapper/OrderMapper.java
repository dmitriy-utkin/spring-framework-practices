package com.practice.order.mapper;

import com.practice.order.event.OrderEvent;
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
                .status(request.getStatus())
                .build();
    }

    public OrderResponse orderToResponse(Order order) {
        return OrderResponse.builder()
                .product(order.getProduct())
                .quantity(order.getQuantity())
                .status(order.getStatus().toString())
                .build();
    }

    public OrderEvent orderToOrderEvent(Order order) {
        return OrderEvent.builder()
                .product(order.getProduct())
                .quantity(order.getQuantity())
                .status(order.getStatus())
                .build();
    }

    public Order orderEventToOrder(OrderEvent event) {
        return Order.builder()
                .product(event.getProduct())
                .quantity(event.getQuantity())
                .status(event.getStatus())
                .build();
    }
}
