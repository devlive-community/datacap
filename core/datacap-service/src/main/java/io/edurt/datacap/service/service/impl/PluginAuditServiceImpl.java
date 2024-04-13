package io.edurt.datacap.service.service.impl;

import com.google.common.collect.Maps;
import com.google.inject.Injector;
import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.utils.SpiUtils;
import io.edurt.datacap.fs.FsRequest;
import io.edurt.datacap.fs.FsResponse;
import io.edurt.datacap.service.activity.HeatmapActivity;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.PluginAuditEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.initializer.InitializerConfigure;
import io.edurt.datacap.service.itransient.ContributionRadar;
import io.edurt.datacap.service.repository.BaseRepository;
import io.edurt.datacap.service.repository.PluginAuditRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.PluginAuditService;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PluginAuditServiceImpl
        implements PluginAuditService
{
    private final PluginAuditRepository pluginAuditRepository;
    private final InitializerConfigure initializer;
    private final Injector injector;

    public PluginAuditServiceImpl(PluginAuditRepository pluginAuditRepository, InitializerConfigure initializer, Injector injector)
    {
        this.pluginAuditRepository = pluginAuditRepository;
        this.initializer = initializer;
        this.injector = injector;
    }

    @Override
    public CommonResponse<PageEntity<PluginAuditEntity>> getAll(BaseRepository repository, FilterBody filter)
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

    @Override
    public CommonResponse<Object> getData(String code)
    {
        return this.pluginAuditRepository.findByCode(code)
                .map(value -> {
                    Response response = new Response();
                    FsRequest fsRequest = FsRequest.builder()
                            .access(initializer.getFsConfigure().getAccess())
                            .secret(initializer.getFsConfigure().getSecret())
                            .endpoint(value.getHome())
                            .bucket(initializer.getFsConfigure().getBucket())
                            .fileName("result.csv")
                            .build();
                    FsResponse fsResponse = SpiUtils.findFs(injector, initializer.getFsConfigure().getType())
                            .map(v -> v.reader(fsRequest))
                            .get();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(fsResponse.getContext()))) {
                        String headersLine = reader.readLine();
                        List<String> headers = Arrays.asList(headersLine.split(","));
                        response.setHeaders(headers);

                        List<Object> columns = new ArrayList<>();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] columnData = line.split(",");
                            Map<String, String> row = Maps.newHashMap();
                            for (int i = 0; i < headers.size(); i++) {
                                row.put(headers.get(i), columnData[i]);
                            }
                            columns.add(row);
                        }
                        response.setColumns(columns);
                    }
                    catch (Exception e) {
                        log.warn("Reader cache failed on [ {} ]", code, e);
                    }
                    return CommonResponse.success(response);
                })
                .orElseGet(() -> CommonResponse.failure(String.format("Not found [ %s ] history", code)));
    }
}
