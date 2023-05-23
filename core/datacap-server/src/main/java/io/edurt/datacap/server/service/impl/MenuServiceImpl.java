package io.edurt.datacap.server.service.impl;

import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.repository.admin.MenuRepository;
import io.edurt.datacap.server.service.MenuService;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl
        implements MenuService
{
    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository)
    {
        this.menuRepository = menuRepository;
    }

    @Override
    public Response<Long> delete(Long id)
    {
        return null;
    }
}
