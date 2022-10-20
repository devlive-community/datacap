package io.edurt.datacap.server.repository;

import io.edurt.datacap.server.entity.PluginAuditEntity;
import io.edurt.datacap.server.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PluginAuditRepository
        extends PagingAndSortingRepository<PluginAuditEntity, Long>
{
    Page<PluginAuditEntity> findAllByUser(UserEntity user, Pageable pageable);

    Long countByUser(UserEntity user);
}
