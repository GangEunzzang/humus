package com.humus.application.order.service;

import com.humus.application.order.port.in.OrderCommandUseCase;
import com.humus.application.order.port.out.OrderExternalPort;
import com.humus.domain.order.Order;
import com.humus.exception.SendOrderException;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderExternalService {

    private final OrderExternalPort orderExternalPort;
    private final OrderCommandUseCase orderCommandUseCase;

    @Retry(name = "external-order", fallbackMethod = "handleFetchFailure")
    public List<Order> fetch() {
        List<Order> fetch = orderExternalPort.fetch();
        orderCommandUseCase.saveAll(fetch);
        return fetch;
    }

    @Retry(name = "external-order", fallbackMethod = "handleSendFailure")
    public void send(Order order) {
        orderExternalPort.send(order);
    }

    private List<Order> handleFetchFailure(Throwable e) {
        log.error("Order fetch failure fallback execution", e);
        return Collections.emptyList();
    }

    private void handleSendFailure(Order order, Throwable e)  {
        log.error("Order send failure fallback execution, orderId={}", order.getId(), e);
        throw new SendOrderException("Failed to send order, orderId={} " + order.getId());
    }
}
