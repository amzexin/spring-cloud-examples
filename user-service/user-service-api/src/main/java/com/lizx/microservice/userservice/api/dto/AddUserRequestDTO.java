package com.lizx.microservice.userservice.api.dto;

import lombok.Data;

@Data
public class AddUserRequestDTO {
    private String username;
    private String password;
}
