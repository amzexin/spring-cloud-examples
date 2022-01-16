package com.lizx.microservice.userservice;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        MDC.put("trace_id", "main" + System.currentTimeMillis());
        SpringApplication.run(UserServiceApplication.class, args);

        log.info(" ==>> application startup successful ...");
        log.info(" ==>> application startup successful ...");
        log.info(" ==>> application startup successful ...");
        MDC.remove("trace_id");
    }

}
