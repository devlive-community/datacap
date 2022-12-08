package io.edurt.datacap.server.service;

import io.edurt.datacap.server.body.FilterBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.PluginAuditEntity;
import io.edurt.datacap.server.itransient.ContributionHistory;
import io.edurt.datacap.server.itransient.ContributionRadar;

import java.util.List;

public interface PluginAuditService
{
    @Deprecated
    Response<PageEntity<PluginAuditEntity>> getAll(int offset, int limit);

    Response<PageEntity<PluginAuditEntity>> getAllByFilter(FilterBody filter);

    Response<Long> count();

    Response<List<ContributionHistory>> getAllContribution();

    Response<List<ContributionRadar>> getContributionRadar();

    Response<PluginAuditEntity> getById(Long id);
}
