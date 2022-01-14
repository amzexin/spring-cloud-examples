package com.lizx.microservice.userservice.api.dto;

import lombok.Data;

/**
 * 微服务通用返回结果
 * TODO 需要提取到公共的jar包中
 * TODO HTTP接口监控的工具待导入
 */
@Data
public class CommonResult<T> {
    private int code;
    private String message;
    private T data;

    public static CommonResult<Object> success() {
        CommonResult<Object> result = new CommonResult<>();
        result.setCode(200);
        result.setMessage("success");
        return result;
    }

    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static CommonResult<Object> failed(Exception e) {
        CommonResult<Object> result = new CommonResult<>();
        result.setCode(500);
        result.setMessage("success exception: " + e.getMessage());
        return result;
    }
}
