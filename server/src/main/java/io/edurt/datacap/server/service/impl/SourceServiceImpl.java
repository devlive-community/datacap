package io.edurt.datacap.server.service.impl;

import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import io.edurt.datacap.server.entity.SourceEntity;
import io.edurt.datacap.server.repository.SourceRepository;
import io.edurt.datacap.server.service.SourceService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SourceServiceImpl
        implements SourceService
{
    private final SourceRepository sourceRepository;

    public SourceServiceImpl(SourceRepository sourceRepository)
    {
        this.sourceRepository = sourceRepository;
    }

    @Override
    public Response<SourceEntity> saveOrUpdate(SourceEntity configure)
    {
        return Response.success(this.sourceRepository.save(configure));
    }

    @Override
    public Response<PageEntity<SourceEntity>> getAll(int offset, int limit)
    {
        Pageable pageable = PageRequest.of(offset, limit);
        return Response.success(PageEntity.build(this.sourceRepository.findAll(pageable)));
    }

    @Override
    public Response<Long> delete(Long id)
    {
        this.sourceRepository.deleteById(id);
        return Response.success(id);
    }
}
