package com.humus.application.order.port.in;

import com.humus.domain.order.Order;

import java.util.List;

public interface OrderCommandUseCase {

    Order save(Order order);

    void saveAll(List<Order> orders);

}
