package io.edurt.datacap.service.repository.admin;

import io.edurt.datacap.common.enums.MenuEnum;
import io.edurt.datacap.service.entity.MenuEntity;
import io.edurt.datacap.service.repository.BaseRepository;

import java.util.List;

public interface MenuRepository
        extends BaseRepository<MenuEntity, Long>
{
    List<MenuEntity> findAllByType(MenuEnum type);
}
