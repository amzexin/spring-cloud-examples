package com.lizx.microservice.userserviceconsumer;

import com.lizx.microservice.userservice.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Slf4j
@EnableFeignClients(clients = {UserService.class})
@EnableDiscoveryClient
@SpringBootApplication
public class UserServiceConsumerApplication {

    public static void main(String[] args) {
        MDC.put("trace_id", "main");
        SpringApplication.run(UserServiceConsumerApplication.class, args);
        MDC.remove("trace_id");

        log.info(" ==>> application startup successful ...");
        log.info(" ==>> application startup successful ...");
        log.info(" ==>> application startup successful ...");
    }

}
