package io.edurt.datacap.server.service;

import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.PluginAuditEntity;
import io.edurt.datacap.server.itransient.ContributionHistory;

import java.util.List;

public interface PluginAuditService
{
    Response<PageEntity<PluginAuditEntity>> getAll(int offset, int limit);

    Response<Long> count();

    Response<List<ContributionHistory>> getAllContribution();
}
