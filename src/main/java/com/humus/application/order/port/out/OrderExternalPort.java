package com.humus.application.order.port.out;

import com.humus.domain.order.Order;
import java.util.List;

public interface OrderExternalPort {

    List<Order> fetch();

    void send(Order order);

}
