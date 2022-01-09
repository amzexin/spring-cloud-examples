package com.lizx.microservice.userservice.consumer.web.controller;

import com.lizx.microservice.userservice.api.dto.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CallController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/call/user/query")
    public Object query() {
        return restTemplate.getForObject("http://user-service/user/query", CommonResult.class);
    }
}
