package io.edurt.datacap.server.service;

import io.edurt.datacap.server.adapter.PageRequestAdapter;
import io.edurt.datacap.server.body.FilterBody;
import io.edurt.datacap.server.common.Response;
import io.edurt.datacap.server.entity.PageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BaseService<T>
{
    default Response<PageEntity<T>> getAll(PagingAndSortingRepository repository, FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        return Response.success(PageEntity.build(repository.findAll(pageable)));
    }

    default Response<T> getById(PagingAndSortingRepository repository, Long id)
    {
        return Response.success(repository.findById(id));
    }

    default Response<T> saveOrUpdate(PagingAndSortingRepository repository, T configure)
    {
        return Response.success(repository.save(configure));
    }

    Response<Long> delete(Long id);
}
