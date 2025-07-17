package com.humus.archunit.application.order.port.out;

import com.humus.OrderTestConfig;
import com.humus.application.order.port.out.OrderRepository;
import com.humus.domain.order.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import(OrderTestConfig.class)
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository.clear();
    }

    @Test
    void save() {
        Order order = Order.create("customer1");
        orderRepository.save(order);

        Order found = orderRepository.findById(1L).orElseThrow();
        assertThat(found).isNotNull();
    }

    @Test
    void findById() {
        Order order = Order.create("customer2");
        orderRepository.save(order);

        Order found = orderRepository.findById(1L).orElseThrow();

        assertThat(found.getCustomerName()).isEqualTo(order.getCustomerName());
    }

    @Test
    void findByIdNotFound() {
        Order found = orderRepository.findById(999L).orElse(null);

        assertThat(found).isNull();
    }

    @Test
    void findAll() {
        orderRepository.save(Order.create("customer3"));
        orderRepository.save(Order.create("customer4"));

        var orders = orderRepository.findAll();

        assertThat(orders).hasSize(2);
        assertThat(orders).extracting(Order::getCustomerName)
                .containsExactlyInAnyOrder("customer3", "customer4");
    }

    @Test
    void clear() {
        orderRepository.save(Order.create("customer5"));
        orderRepository.clear();

        var orders = orderRepository.findAll();

        assertThat(orders).isEmpty();
    }


}
