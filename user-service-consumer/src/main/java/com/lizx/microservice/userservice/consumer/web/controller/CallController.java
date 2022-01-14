package com.lizx.microservice.userservice.consumer.web.controller;

import com.lizx.microservice.userservice.api.dto.CommonResult;
import com.lizx.microservice.userservice.api.dto.QueryUserRequestDTO;
import com.lizx.microservice.userservice.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class CallController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    private UserService userService;

    @GetMapping("/call/user/get/{userId}")
    public Object query(@PathVariable("userId") int userId) {
        /**
         * 比较原始的调用方法
         */
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
        ServiceInstance serviceInstance = instances.get(0);
        String url = String.format("%s/user/get/%d", serviceInstance.getUri(), userId);
        return restTemplate.getForObject(url, CommonResult.class);
    }

    @GetMapping("/call/user/query2")
    public Object query2() {
        QueryUserRequestDTO requestDTO = new QueryUserRequestDTO();
        requestDTO.setUsername("xxx");
        return userService.queryUser(requestDTO);
    }
}
