package com.practice.order.mapper;

import com.practice.order.event.OrderEvent;
import com.practice.order.model.Order;
import com.practice.order.web.model.OrderListResponse;
import com.practice.order.web.model.OrderResponse;
import com.practice.order.web.model.UpsertOrder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class OrderMapper {

    public Order requestToOrder(UpsertOrder request) {
        return Order.builder()
                .product(request.getProduct())
                .quantity(request.getQuantity())
                .status(request.getStatus())
                .build();
    }

    public OrderListResponse orderListToOrderListResponse(List<Order> orderList) {
        return OrderListResponse.builder()
                .orders(orderList.stream().map(this::orderToResponse).toList())
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
                .date(Instant.now())
                .order(order)
                .build();
    }

    public Order orderEventToOrder(OrderEvent event) {
        return Order.builder()
                .product(event.getOrder().getProduct())
                .quantity(event.getOrder().getQuantity())
                .status(event.getOrder().getStatus())
                .build();
    }
}
