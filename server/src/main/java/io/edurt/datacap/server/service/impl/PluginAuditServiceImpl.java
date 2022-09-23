package io.edurt.datacap.server.service.impl;

import io.edurt.datacap.server.adapter.PageRequestAdapter;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.PluginAuditEntity;
import io.edurt.datacap.server.repository.PluginAuditRepository;
import io.edurt.datacap.server.service.PluginAuditService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PluginAuditServiceImpl
        implements PluginAuditService
{
    private final PluginAuditRepository pluginAuditRepository;

    public PluginAuditServiceImpl(PluginAuditRepository pluginAuditRepository)
    {
        this.pluginAuditRepository = pluginAuditRepository;
    }

    @Override
    public Response<PageEntity<PluginAuditEntity>> getAll(int offset, int limit)
    {
        Pageable pageable = PageRequestAdapter.of(offset, limit);
        return Response.success(PageEntity.build(this.pluginAuditRepository.findAll(pageable)));
    }
}
