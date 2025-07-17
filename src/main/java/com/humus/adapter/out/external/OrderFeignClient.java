package com.humus.adapter.out.external;

import com.humus.config.FeignConfig;
import com.humus.domain.order.Order;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "orderClient",
        url = "${external.service.endpoint}",
        path = "/api",
        configuration = FeignConfig.class
)
public interface OrderFeignClient {

    @GetMapping("/fetch")
    List<Order> fetch();

    @PostMapping("/send")
    void send(@RequestBody Order order);
}
