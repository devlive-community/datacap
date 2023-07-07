package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.service.repository.admin.MenuRepository;
import io.edurt.datacap.service.service.MenuService;
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
}
