package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.activity.HeatmapActivity;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.PluginAuditEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.itransient.ContributionRadar;
import io.edurt.datacap.service.repository.PluginAuditRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.PluginAuditService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public CommonResponse<PageEntity<PluginAuditEntity>> getAll(int offset, int limit)
    {
        Pageable pageable = PageRequestAdapter.of(offset, limit);
        return CommonResponse.success(PageEntity.build(this.pluginAuditRepository.findAllByUser(UserDetailsService.getUser(), pageable)));
    }

    @Override
    public CommonResponse<PageEntity<PluginAuditEntity>> getAllByFilter(FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        return CommonResponse.success(PageEntity.build(this.pluginAuditRepository.findAllByUser(UserDetailsService.getUser(), pageable)));
    }

    @Override
    public CommonResponse<Long> count()
    {
        return CommonResponse.success(this.pluginAuditRepository.countByUser(UserDetailsService.getUser()));
    }

    @Override
    public CommonResponse<List<HeatmapActivity>> getAllContribution()
    {
        UserEntity user = UserDetailsService.getUser();
        return CommonResponse.success(this.pluginAuditRepository.countByCreateTimeAndFindByUser(user));
    }

    @Override
    public CommonResponse<List<ContributionRadar>> getContributionRadar()
    {
        UserEntity user = UserDetailsService.getUser();
        List<ContributionRadar> contributionRadars = new ArrayList<>();
        List<Map<String, Object>> list = this.pluginAuditRepository.selectRadarByUserId(user.getId());
        Long count = list.stream().mapToLong(v -> Long.valueOf(String.valueOf(v.get("dataOfCount")))).sum();
        list.forEach(v -> {
            ContributionRadar radar = new ContributionRadar();
            radar.setLabel(String.valueOf(v.get("dataOfLabel")));
            radar.setCount(Long.valueOf(String.valueOf(v.get("dataOfCount"))));
            BigDecimal left = new BigDecimal(radar.getCount());
            BigDecimal right = new BigDecimal(count);
            BigDecimal divide = left.divide(right, 2, RoundingMode.HALF_UP);
            radar.setPercentage(Float.valueOf(divide.toString()) * 100);
            contributionRadars.add(radar);
        });
        return CommonResponse.success(contributionRadars);
    }

    @Override
    public CommonResponse<PluginAuditEntity> getById(Long id)
    {
        return CommonResponse.success(this.pluginAuditRepository.findById(id));
    }
}
