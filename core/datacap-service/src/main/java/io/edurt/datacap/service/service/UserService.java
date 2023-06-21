package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.response.JwtResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.UserNameBody;
import io.edurt.datacap.service.body.UserPasswordBody;
import io.edurt.datacap.service.body.UserQuestionBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.record.TreeRecord;

import java.util.List;
import java.util.Map;

public interface UserService
{
    CommonResponse<UserEntity> saveOrUpdate(UserEntity configure);

    CommonResponse<JwtResponse> authenticate(UserEntity configure);

    CommonResponse<UserEntity> info(Long userId);

    CommonResponse<Long> changePassword(UserPasswordBody configure);

    CommonResponse<Long> changeUsername(UserNameBody configure);

    CommonResponse<Long> changeThirdConfigure(Map<String, Map<String, Object>> configure);

    CommonResponse<Object> startChat(UserQuestionBody configure);

    CommonResponse<List<Object>> getSugs(Long id);

    CommonResponse<List<TreeRecord>> getMenus();

    CommonResponse<PageEntity<UserEntity>> getAll(FilterBody filter);
}
