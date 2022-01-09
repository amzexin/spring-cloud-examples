package com.lizx.microservice.userservice.api.service;

import com.lizx.microservice.userservice.api.dto.*;
import com.lizx.xxx.api.dto.*;

public interface UserService {

    CommonResult<AddUserResponseDTO> addUser(AddUserRequestDTO addUserRequestDTO);

    CommonResult<Object> deleteUser(DeleteUserRequestDTO deleteUserRequestDTO);

    CommonResult<Object> updateUser(UpdateUserRequestDTO updateUserRequestDTO);

    CommonResult<QueryUserResponseDTO> queryUser(QueryUserRequestDTO queryUserRequestDTO);

}
