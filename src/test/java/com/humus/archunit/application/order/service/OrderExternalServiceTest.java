package com.humus.archunit.application.order.service;

import com.humus.application.order.port.out.OrderRepository;
import com.humus.application.order.service.OrderExternalService;
import com.humus.domain.order.Order;
import com.humus.domain.order.OrderStatus;
import com.humus.exception.SendOrderException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(
        properties = {
                "external.service.endpoint=http://localhost:18080",
                "spring.cloud.openfeign.client.config.default.connect-timeout=500",
                "spring.cloud.openfeign.client.config.default.read-timeout=500",
        }
)
@EnableFeignClients
@Import({OrderExternalService.class})
class OrderExternalServiceTest {

    static MockWebServer mockWebServer;

    @Autowired
    OrderExternalService orderExternalService;

    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    void setUpBefore() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(18080);
        orderRepository.clear();
    }

    @AfterEach
    void setUpAfter() throws IOException {
        mockWebServer.shutdown();
    }

    @DisplayName("fetch할 때 주문 목록을 가져온 후 db에 저장한다.")
    @Test
    void fetch_shouldReturnOrderList_SaveAll() throws Exception {
        // given
        String jsonBody = """
                [
                  {
                    "id": 1,
                    "customerName": "customer1",
                    "orderDate": "2025-07-17",
                    "status": "PROCESSING"
                  },
                  {
                    "id": 2,
                    "customerName": "customer2",
                    "orderDate": "2025-07-18",
                    "status": "SHIPPING"
                  }
                ]
                """;

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonBody)
                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        // when
        orderExternalService.fetch();

        // then
        List<Order> all = orderRepository.findAll();
        Order order = all.getFirst();

        assertThat(all).hasSize(2);
        assertThat(order.getId()).isEqualTo(1L);
        assertThat(order.getCustomerName()).isEqualTo("customer1");
        assertThat(order.getOrderDate()).isEqualTo(LocalDate.of(2025, 7, 17));
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PROCESSING);
    }

    @DisplayName("fetch할 때 실패하면 빈 리스트를 반환한다.")
    @Test
    void fetch_shouldReturnEmptyList_whenExceptionOccurs() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        List<Order> result = orderExternalService.fetch();

        assertThat(result).isEmpty();
    }

    @DisplayName("fetch할 때 재시도 가능한 예외가 발생하면 재시도한다.")
    @Test
    void fetch_shouldHandleExternalServerError() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        List<Order> result = orderExternalService.fetch();

        assertThat(result).isEmpty();
        assertThat(mockWebServer.getRequestCount()).isEqualTo(3);
    }

    @DisplayName("send 메서드가 주문을 외부 서버로 전송한다.")
    @Test
    void send_shouldSendCorrectJsonBody() throws Exception {
        Order order = createOrder();

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{}")
                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        orderExternalService.send(order);

        var recordedRequest = mockWebServer.takeRequest();
        String sentBody = recordedRequest.getBody().readUtf8();
        assertThat(sentBody).contains("\"customerName\":\"customer1\"");
        assertThat(sentBody).contains("\"orderDate\":\"2025-07-17\"");
        assertThat(sentBody).contains("\"status\":\"SHIPPING\"");
        assertThat(recordedRequest.getHeader("Content-Type")).contains("application/json");
    }

    @DisplayName("send 메서드가 실패하면 SendOrderException을 던진다.")
    @Test
    void send_shouldRetryOnFailureAndEventuallyThrow() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        Order order = createOrder();

        assertThatThrownBy(() -> orderExternalService.send(order))
                .isInstanceOf(SendOrderException.class);
    }

    @DisplayName("send 메서드 실행시 재시도 가능한 예외가 발생하면 재시도한다.")
    @Test
    void send_shouldRetryOnRetryableException() throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));

        Order order = createOrder();

        orderExternalService.send(order);

        assertThat(mockWebServer.getRequestCount()).isEqualTo(3);
    }


    private Order createOrder() {
        return Order.createWithId(11L, "customer1", LocalDate.of(2025, 7, 17), OrderStatus.SHIPPING);
    }
}
