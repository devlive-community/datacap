package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.activity.HeatmapActivity;
import io.edurt.datacap.service.entity.PluginAuditEntity;
import io.edurt.datacap.service.itransient.ContributionRadar;

import java.util.List;

public interface PluginAuditService
        extends BaseService<PluginAuditEntity>
{
    CommonResponse<Long> count();

    CommonResponse<List<HeatmapActivity>> getAllContribution();

    CommonResponse<List<ContributionRadar>> getContributionRadar();

    CommonResponse<PluginAuditEntity> getById(Long id);

    CommonResponse<Object> getData(Long id);
}
