package com.humus.application.order.port.in;

import com.humus.domain.order.Order;

public interface OrderCommandUseCase {

    Order save(Order order);
}
