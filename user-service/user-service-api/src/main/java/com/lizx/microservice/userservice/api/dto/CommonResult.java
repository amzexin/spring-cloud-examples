package com.lizx.microservice.userservice.api.dto;

import lombok.Data;

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
}
