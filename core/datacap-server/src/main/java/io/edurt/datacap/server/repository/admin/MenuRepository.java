package io.edurt.datacap.server.repository.admin;

import io.edurt.datacap.server.common.MenuEnum;
import io.edurt.datacap.server.entity.admin.MenuEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MenuRepository
        extends PagingAndSortingRepository<MenuEntity, Long>
{
    List<MenuEntity> findAllByType(MenuEnum type);
}
