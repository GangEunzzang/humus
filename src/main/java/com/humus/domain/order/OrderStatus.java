package com.humus.domain.order;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum OrderStatus {
    PROCESSING("처리 중"),
    SHIPPING("배송 중"),
    COMPLETED("배송 완료")

    ;

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public static OrderStatus fromString(String status) {
        return Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 주문 상태: " + status));
    }
}
