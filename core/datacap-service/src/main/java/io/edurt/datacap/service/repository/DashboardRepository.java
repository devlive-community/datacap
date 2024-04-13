package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.DashboardEntity;
import io.edurt.datacap.service.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DashboardRepository
        extends BaseRepository<DashboardEntity, Long>
{
    Page<DashboardEntity> findAllByUser(UserEntity user, Pageable pageable);
}
