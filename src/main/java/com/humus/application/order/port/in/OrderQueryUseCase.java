package com.humus.application.order.port.in;

import com.humus.domain.order.Order;

import java.util.List;

public interface OrderQueryUseCase {

    Order find(Long id);

    List<Order> findAll();
}
