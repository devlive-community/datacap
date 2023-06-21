package io.edurt.datacap.service.repository.admin;

import io.edurt.datacap.common.enums.MenuEnum;
import io.edurt.datacap.service.entity.MenuEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MenuRepository
        extends PagingAndSortingRepository<MenuEntity, Long>
{
    List<MenuEntity> findAllByType(MenuEnum type);
}
