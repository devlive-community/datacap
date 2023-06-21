package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.PageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BaseService<T>
{
    default CommonResponse<PageEntity<T>> getAll(PagingAndSortingRepository repository, FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        return CommonResponse.success(PageEntity.build(repository.findAll(pageable)));
    }

    default CommonResponse<T> getById(PagingAndSortingRepository repository, Long id)
    {
        return CommonResponse.success(repository.findById(id));
    }

    default CommonResponse<T> saveOrUpdate(PagingAndSortingRepository repository, T configure)
    {
        return CommonResponse.success(repository.save(configure));
    }

    CommonResponse<Long> delete(Long id);
}
