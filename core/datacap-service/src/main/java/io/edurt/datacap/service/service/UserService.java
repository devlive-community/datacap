package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.UserNameBody;
import io.edurt.datacap.service.body.UserPasswordBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.model.AiModel;
import io.edurt.datacap.service.record.TreeRecord;

import java.util.List;

public interface UserService
{
    CommonResponse<UserEntity> saveOrUpdate(UserEntity configure);

    CommonResponse<Object> authenticate(UserEntity configure);

    CommonResponse<UserEntity> info(Long userId);

    CommonResponse<Long> changePassword(UserPasswordBody configure);

    CommonResponse<Long> changeUsername(UserNameBody configure);

    CommonResponse<Long> changeThirdConfigure(AiModel configure);

    CommonResponse<List<Object>> getSugs(Long id);

    CommonResponse<List<TreeRecord>> getMenus();

    CommonResponse<PageEntity<UserEntity>> getAll(FilterBody filter);
}
