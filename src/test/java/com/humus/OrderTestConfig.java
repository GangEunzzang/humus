package com.humus;

import com.humus.adapter.out.persistence.InMemoryOrderRepository;
import com.humus.application.order.port.out.OrderRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class OrderTestConfig {

    @Bean
    public OrderRepository orderRepository() {
        return new InMemoryOrderRepository();
    }

}
