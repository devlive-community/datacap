package io.edurt.datacap.service.service.impl;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.DataSetEntity;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.repository.DataSetRepository;
import io.edurt.datacap.service.security.UserDetailsService;
import io.edurt.datacap.service.service.DataSetService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

@Service
public class DataSetServiceImpl
        implements DataSetService
{
    private final DataSetRepository repository;

    public DataSetServiceImpl(DataSetRepository repository)
    {
        this.repository = repository;
    }

    @Override
    public CommonResponse<DataSetEntity> saveOrUpdate(PagingAndSortingRepository repository, DataSetEntity configure)
    {
        configure.setUser(UserDetailsService.getUser());
        return CommonResponse.success(repository.save(configure));
    }

    @Override
    public CommonResponse<PageEntity<DataSetEntity>> getAll(FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        return CommonResponse.success(PageEntity.build(repository.findAllByUser(UserDetailsService.getUser(), pageable)));
    }
}
