package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.entity.ReportEntity;
import io.edurt.datacap.service.repository.ReportRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.ReportService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl
        implements ReportService
{
    private final ReportRepository repository;

    public ReportServiceImpl(ReportRepository repository)
    {
        this.repository = repository;
    }

    @Override
    public CommonResponse<PageEntity<ReportEntity>> getAll(FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        return CommonResponse.success(PageEntity.build(repository.findAllByUser(UserDetailsService.getUser(), pageable)));
    }
}
