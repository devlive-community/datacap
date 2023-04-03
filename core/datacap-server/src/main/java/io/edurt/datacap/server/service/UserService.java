package io.edurt.datacap.server.service;

import io.edurt.datacap.server.body.UserNameBody;
import io.edurt.datacap.server.body.UserPasswordBody;
import io.edurt.datacap.server.body.UserQuestionBody;
import io.edurt.datacap.server.common.JwtResponse;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.UserEntity;

import java.util.List;
import java.util.Map;

public interface UserService
{
    Response<UserEntity> saveOrUpdate(UserEntity configure);

    Response<JwtResponse> authenticate(UserEntity configure);

    Response<UserEntity> info(Long userId);

    Response<Long> changePassword(UserPasswordBody configure);

    Response<Long> changeUsername(UserNameBody configure);

    Response<Long> changeThirdConfigure(Map<String, Map<String, Object>> configure);

    Response<Object> startChat(UserQuestionBody configure);

    Response<List<Object>> getSugs(Long id);
}
