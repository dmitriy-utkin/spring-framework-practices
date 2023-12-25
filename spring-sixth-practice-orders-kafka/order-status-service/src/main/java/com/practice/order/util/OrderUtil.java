package com.practice.order.util;

import com.practice.order.model.OrderStatus;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class OrderUtil {

    public OrderStatus getRandomOrderStatus() {
        List<OrderStatus> statuses = new ArrayList<>();
        statuses.add(OrderStatus.PROCESS);
        statuses.add(OrderStatus.CANCELED);
        statuses.add(OrderStatus.FINISHED);
        statuses.add(OrderStatus.RETURNED);
        Collections.shuffle(statuses);
        return statuses.get(0);
    }
}
