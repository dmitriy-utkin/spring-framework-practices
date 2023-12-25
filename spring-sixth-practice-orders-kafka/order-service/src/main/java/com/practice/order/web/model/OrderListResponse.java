package com.practice.order.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderListResponse {

    @Builder.Default
    private List<OrderResponse> orders = new ArrayList<>();
}
