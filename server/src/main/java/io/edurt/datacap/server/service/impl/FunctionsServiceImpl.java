package io.edurt.datacap.server.service.impl;

import io.edurt.datacap.server.adapter.PageRequestAdapter;
import io.edurt.datacap.server.body.FilterBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.FunctionsEntity;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.repository.FunctionsRepository;
import io.edurt.datacap.server.service.FunctionsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FunctionsServiceImpl
        implements FunctionsService
{
    private final FunctionsRepository functionsRepository;

    public FunctionsServiceImpl(FunctionsRepository functionsRepository)
    {
        this.functionsRepository = functionsRepository;
    }

    @Override
    public Response<FunctionsEntity> saveOrUpdate(FunctionsEntity configure)
    {
        return Response.success(this.functionsRepository.save(configure));
    }

    @Override
    public Response<PageEntity<FunctionsEntity>> getAllByFilter(FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        return Response.success(PageEntity.build(this.functionsRepository.findAll(pageable)));
    }

    @Override
    public Response<FunctionsEntity> getById(Long id)
    {
        return Response.success(this.functionsRepository.findById(id));
    }
}
