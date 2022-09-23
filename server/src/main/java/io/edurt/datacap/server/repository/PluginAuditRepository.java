package io.edurt.datacap.server.repository;

import io.edurt.datacap.server.entity.PluginAuditEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PluginAuditRepository
        extends PagingAndSortingRepository<PluginAuditEntity, Long>
{
}
