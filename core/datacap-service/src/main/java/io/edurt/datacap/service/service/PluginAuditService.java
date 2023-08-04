package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.activity.HeatmapActivity;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.PluginAuditEntity;
import io.edurt.datacap.service.itransient.ContributionRadar;

import java.util.List;

public interface PluginAuditService
{
    @Deprecated
    CommonResponse<PageEntity<PluginAuditEntity>> getAll(int offset, int limit);

    CommonResponse<PageEntity<PluginAuditEntity>> getAllByFilter(FilterBody filter);

    CommonResponse<Long> count();

    CommonResponse<List<HeatmapActivity>> getAllContribution();

    CommonResponse<List<ContributionRadar>> getContributionRadar();

    CommonResponse<PluginAuditEntity> getById(Long id);
}
