package com.lizx.microservice.userservice.api.service;

import com.lizx.microservice.userservice.api.dto.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {

    @PostMapping("/user/add")
    CommonResult<AddUserResponseDTO> addUser(@RequestBody AddUserRequestDTO addUserRequestDTO);

    @PostMapping("/user/delete")
    CommonResult<Object> deleteUser(@RequestBody DeleteUserRequestDTO deleteUserRequestDTO);

    @PostMapping("/user/update")
    CommonResult<Object> updateUser(@RequestBody UpdateUserRequestDTO updateUserRequestDTO);

    @GetMapping("/user/query")
    CommonResult<QueryUserResponseDTO> queryUser(@RequestBody QueryUserRequestDTO queryUserRequestDTO);

}
