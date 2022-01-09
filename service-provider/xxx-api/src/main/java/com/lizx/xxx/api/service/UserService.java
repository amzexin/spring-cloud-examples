package com.lizx.xxx.api.service;

import com.lizx.xxx.api.dto.*;

public interface UserService {

    CommonResult<AddUserResponseDTO> addUser(AddUserRequestDTO addUserRequestDTO);

    CommonResult<Object> deleteUser(DeleteUserRequestDTO deleteUserRequestDTO);

    CommonResult<Object> updateUser(UpdateUserRequestDTO updateUserRequestDTO);

    CommonResult<QueryUserResponseDTO> queryUser(QueryUserRequestDTO queryUserRequestDTO);

}
