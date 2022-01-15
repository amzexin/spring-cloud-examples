package com.lizx.microservice.gatewayservice;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayServiceApplication {

    public static void main(String[] args) {
        MDC.put("trace_id", "main");
        SpringApplication.run(GatewayServiceApplication.class, args);
        MDC.remove("trace_id");

        log.info(" ==>> application startup successful ...");
        log.info(" ==>> application startup successful ...");
        log.info(" ==>> application startup successful ...");
    }

}
