package com.humus.application.order.service;

import com.humus.application.order.port.in.OrderCommandUseCase;
import com.humus.application.order.port.out.OrderRepository;
import com.humus.domain.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderCommandService implements OrderCommandUseCase {

    private final OrderRepository orderRepository;

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void saveAll(List<Order> orders) {
        orders.forEach(orderRepository::save);
    }
}
