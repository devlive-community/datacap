package io.edurt.datacap.server.service.impl;

import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.repository.RoleRepository;
import io.edurt.datacap.server.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl
        implements RoleService
{
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository)
    {
        this.roleRepository = roleRepository;
    }

    @Override
    public Response<Long> delete(Long id)
    {
        return null;
    }
}
