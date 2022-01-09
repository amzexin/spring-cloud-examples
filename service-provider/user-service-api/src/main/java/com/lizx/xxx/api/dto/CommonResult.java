package com.lizx.xxx.api.dto;

import lombok.Data;

@Data
public class CommonResult<T> {
    private int code;
    private String message;
    private T data;
}
