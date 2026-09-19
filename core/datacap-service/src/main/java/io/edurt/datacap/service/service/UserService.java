package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.body.UploadBody;
import io.edurt.datacap.service.body.UserNameBody;
import io.edurt.datacap.service.body.UserPasswordBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.entity.convert.AvatarEntity;
import io.edurt.datacap.service.entity.itransient.user.UserEditorEntity;
import io.edurt.datacap.service.model.AiModel;
import io.edurt.datacap.service.record.TreeRecord;

import java.util.List;

public interface UserService
        extends BaseService<UserEntity>
{
    CommonResponse<UserEntity> saveOrUpdate(UserEntity configure);

    CommonResponse<Object> authenticate(UserEntity configure);

    CommonResponse<UserEntity> info(String code);

    CommonResponse<Long> changePassword(UserPasswordBody configure);

    CommonResponse<Long> changeUsername(UserNameBody configure);

    /**
     * Check whether the given username can be used for renaming:
     * format valid (4-20 chars, letters/numbers/underscores, not starting
     * with a digit) and not taken by another account.
     */
    CommonResponse<Boolean> checkUsername(String username);

    CommonResponse<Long> changeThirdConfigure(AiModel configure);

    CommonResponse<List<Object>> getSugs(Long id);

    CommonResponse<List<TreeRecord>> getMenus();

    CommonResponse<PageEntity<UserEntity>> getAll(FilterBody filter);

    CommonResponse<UserEntity> changeEditorConfigure(UserEditorEntity configure);

    CommonResponse<AvatarEntity> uploadAvatar(UploadBody configure);

    CommonResponse<UserEntity> changeNotify(UserEntity configure);
}
