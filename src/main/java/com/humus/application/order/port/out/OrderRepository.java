package com.humus.application.order.port.out;

import com.humus.domain.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long orderId);

    List<Order> findAll();

    void clear();
}
