package com.humus.domain.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    private Long id;
    private String customerName;
    private LocalDate orderDate;
    private OrderStatus status;

    private Order(Long id, String customerName, LocalDate orderDate, OrderStatus status) {
        this.id = id;
        this.customerName = requireNonNull(customerName);
        this.orderDate = requireNonNull(orderDate);
        this.status = requireNonNull(status);
    }

    public static Order create(String customerName) {
        Order order = new Order();

        order.id = null;
        order.customerName = customerName;
        order.orderDate = LocalDate.now();
        order.status = OrderStatus.PROCESSING;

        return order;
    }

    public static Order createWithId(Long id, String customerName, LocalDate orderDate, OrderStatus status) {
        Order order = new Order();

        order.id = requireNonNull(id);
        order.customerName = customerName;
        order.orderDate = orderDate;
        order.status = status;

        return order;
    }




}
