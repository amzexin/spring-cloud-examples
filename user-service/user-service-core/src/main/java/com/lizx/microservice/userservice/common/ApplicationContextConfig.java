package com.lizx.microservice.userservice.common;

import com.lizx.common.requestlogger.http.HttpRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationContextConfig {

    @Bean
    public FilterRegistrationBean<HttpRequestFilter> webRequestFilter() {
        FilterRegistrationBean<HttpRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new HttpRequestFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }
}
