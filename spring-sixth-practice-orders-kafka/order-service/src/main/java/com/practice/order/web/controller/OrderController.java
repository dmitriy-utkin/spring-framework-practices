package com.practice.order.web.controller;

import com.practice.order.mapper.OrderMapper;
import com.practice.order.service.OrderService;
import com.practice.order.web.model.OrderResponse;
import com.practice.order.web.model.UpsertOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<OrderResponse> save(@RequestBody UpsertOrder request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                orderMapper.orderToResponse(
                        orderService.save(orderMapper.requestToOrder(request))
                )
        );
    }
}
