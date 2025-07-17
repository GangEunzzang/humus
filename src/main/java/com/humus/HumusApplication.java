package com.humus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class HumusApplication {

    public static void main(String[] args) {
        SpringApplication.run(HumusApplication.class, args);
    }

}
