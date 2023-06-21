package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.entity.RoleEntity;

public interface RoleService
        extends BaseService<RoleEntity>
{
    CommonResponse<Object> getMenusByRoleId(Long roleId);
}
