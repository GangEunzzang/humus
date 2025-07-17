package com.humus.archunit.application.order.port.in;

import com.humus.application.order.port.in.OrderCommandUseCase;
import com.humus.application.order.port.in.OrderQueryUseCase;
import com.humus.application.order.port.out.OrderRepository;
import com.humus.domain.order.Order;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
class OrderQueryUseCaseTest {

    @Autowired
    private OrderCommandUseCase orderCommandUseCase;

    @Autowired
    private OrderQueryUseCase orderQueryUseCase;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository.clear();
    }


    @Test
    void find() {
        Order order = Order.create("customer1");
        Order saved = orderCommandUseCase.save(order);

        Order found = orderQueryUseCase.find(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals(saved.getCustomerName(), found.getCustomerName());
    }

    @Test
    void findAll() {
        Order order1 = Order.create("customer1");
        Order order2 = Order.create("customer2");

        orderCommandUseCase.saveAll(List.of(order1, order2));

        assertEquals(2, orderQueryUseCase.findAll().size());
    }


}
