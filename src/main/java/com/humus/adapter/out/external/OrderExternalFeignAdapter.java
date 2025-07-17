package com.humus.adapter.out.external;

import com.humus.application.order.port.out.OrderExternalPort;
import com.humus.domain.order.Order;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderExternalFeignAdapter implements OrderExternalPort {

    private final OrderFeignClient orderFeignClient;

    @Override
    public List<Order> fetch() {
        return orderFeignClient.fetch();
    }

    @Override
    public void send(Order order) {
        orderFeignClient.send(order);
    }
}