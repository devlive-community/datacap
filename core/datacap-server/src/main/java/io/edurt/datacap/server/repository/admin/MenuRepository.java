package io.edurt.datacap.server.repository.admin;

import io.edurt.datacap.server.entity.admin.MenuEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MenuRepository
        extends PagingAndSortingRepository<MenuEntity, Long>
{
}
