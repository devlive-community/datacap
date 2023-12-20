package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.DashboardEntity;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.repository.DashboardRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.DashboardService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl
        implements DashboardService
{
    private final DashboardRepository repository;

    public DashboardServiceImpl(DashboardRepository repository)
    {
        this.repository = repository;
    }

    @Override
    public CommonResponse<PageEntity<DashboardEntity>> getAll(FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        return CommonResponse.success(PageEntity.build(repository.findAllByUser(UserDetailsService.getUser(), pageable)));
    }

    @Override
    public CommonResponse<DashboardEntity> saveOrUpdate(PagingAndSortingRepository repository, DashboardEntity configure)
    {
        configure.setUser(UserDetailsService.getUser());
        return CommonResponse.success(repository.save(configure));
    }
}
