package com.lizx.microservice.userservice.common;

import com.lizx.common.bean.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Description: ExceptionInterceptor
 *
 * @author Lizexin
 * @date 2022-01-14 14:08
 */
@Slf4j
@RestControllerAdvice
public class ExceptionInterceptor {

    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(Exception e) {
        log.error(e.getMessage(), e);
        return CommonResult.failed(e);
    }
}
