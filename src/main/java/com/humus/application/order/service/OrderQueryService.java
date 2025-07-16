package com.humus.application.order.service;

import com.humus.application.order.port.in.OrderQueryUseCase;
import com.humus.application.order.port.out.OrderRepository;
import com.humus.domain.order.Order;
import com.humus.domain.order.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderQueryService implements OrderQueryUseCase {

    private final OrderRepository orderRepository;

    @Override
    public Order find(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
