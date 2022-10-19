package io.edurt.datacap.server.service;

import io.edurt.datacap.server.common.JwtResponse;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.UserEntity;

public interface UserService
{
    Response<UserEntity> saveOrUpdate(UserEntity configure);

    Response<JwtResponse> authenticate(UserEntity configure);
}
