package com.humus.adapter.out.persistence;

import com.humus.application.order.port.out.OrderRepository;
import com.humus.domain.order.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryOrderRepository implements OrderRepository {

    private final Map<Long, Order> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1L);

    @Override
    public Order save(Order order) {
        Long id = sequence.getAndIncrement();
        Order newOrder = Order.createWithId(
                id,
                order.getCustomerName(),
                order.getOrderDate(),
                order.getStatus()
        );
        store.put(id, newOrder);

        return newOrder;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void clear() {
        store.clear();
        sequence.set(1L);
    }
}

