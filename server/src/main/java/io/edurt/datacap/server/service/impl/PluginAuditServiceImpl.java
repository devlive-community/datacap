package io.edurt.datacap.server.service.impl;

import io.edurt.datacap.server.adapter.PageRequestAdapter;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.PluginAuditEntity;
import io.edurt.datacap.server.entity.UserEntity;
import io.edurt.datacap.server.itransient.ContributionHistory;
import io.edurt.datacap.server.itransient.ContributionRadar;
import io.edurt.datacap.server.repository.PluginAuditRepository;
import io.edurt.datacap.server.security.UserDetailsService;
import io.edurt.datacap.server.service.PluginAuditService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public Response<PageEntity<PluginAuditEntity>> getAll(int offset, int limit)
    {
        Pageable pageable = PageRequestAdapter.of(offset, limit);
        return Response.success(PageEntity.build(this.pluginAuditRepository.findAllByUser(UserDetailsService.getUser(), pageable)));
    }

    @Override
    public Response<Long> count()
    {
        return Response.success(this.pluginAuditRepository.countByUser(UserDetailsService.getUser()));
    }

    @Override
    public Response<List<ContributionHistory>> getAllContribution()
    {
        UserEntity user = UserDetailsService.getUser();
        List<ContributionHistory> contributions = new ArrayList<>();
        this.pluginAuditRepository.selectContributionByUserId(user.getId()).forEach(v -> {
            ContributionHistory contribution = new ContributionHistory();
            contribution.setDay(Integer.valueOf(String.valueOf(v.get("dataOfDay"))));
            contribution.setMonth(Integer.valueOf(String.valueOf(v.get("dataOfMonth"))));
            contribution.setDate(String.valueOf(v.get("dataOfDate")));
            contribution.setCount(Long.valueOf(String.valueOf(v.get("dataOfCount"))));
            contribution.setWeek(String.valueOf(v.get("dataOfWeek")));
            contribution.setLastDay(Boolean.valueOf(String.valueOf(v.get("dataOfLastDay"))));
            contribution.setLastWeek(Boolean.valueOf(String.valueOf(v.get("dataOfLastWeek"))));
            contributions.add(contribution);
        });
        return Response.success(contributions);
    }

    @Override
    public Response<List<ContributionRadar>> getContributionRadar()
    {
        UserEntity user = UserDetailsService.getUser();
        List<ContributionRadar> contributionRadars = new ArrayList<>();
        List<Map<String, Object>> list = this.pluginAuditRepository.selectRadarByUserId(user.getId());
        Long count = list.stream().mapToLong(v -> Long.valueOf(String.valueOf(v.get("dataOfCount")))).sum();
        list.forEach(v -> {
            ContributionRadar radar = new ContributionRadar();
            radar.setLabel(String.valueOf(v.get("dataOfLabel")));
            radar.setCount(Long.valueOf(String.valueOf(v.get("dataOfCount"))));
            BigDecimal bigDecimal = new BigDecimal(Double.valueOf(radar.getCount()) / Double.valueOf(count));
            radar.setPercentage(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100.0);
            contributionRadars.add(radar);
        });
        return Response.success(contributionRadars);
    }
}
