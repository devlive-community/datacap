package io.edurt.datacap.server.service;

import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.RoleEntity;

public interface RoleService
        extends BaseService<RoleEntity>
{
    Response<Object> getMenusByRoleId(Long roleId);
}
