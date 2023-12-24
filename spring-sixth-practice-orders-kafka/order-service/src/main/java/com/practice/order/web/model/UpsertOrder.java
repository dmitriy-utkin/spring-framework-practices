package com.practice.order.web.model;

import com.practice.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpsertOrder {
    private String product;
    private Integer quantity;

    @Builder.Default
    private OrderStatus status = OrderStatus.CREATED;
}
