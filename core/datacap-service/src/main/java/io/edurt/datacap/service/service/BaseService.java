package io.edurt.datacap.service.service;

import io.edurt.datacap.common.response.CommonResponse;
import io.edurt.datacap.common.utils.NullAwareBeanUtils;
import io.edurt.datacap.service.adapter.PageRequestAdapter;
import io.edurt.datacap.service.body.FilterBody;
import io.edurt.datacap.service.entity.BaseEntity;
import io.edurt.datacap.service.entity.PageEntity;
import io.edurt.datacap.service.repository.BaseRepository;
import org.springframework.data.domain.Pageable;

public interface BaseService<T extends BaseEntity>
{
    default CommonResponse<PageEntity<T>> getAll(BaseRepository repository, FilterBody filter)
    {
        Pageable pageable = PageRequestAdapter.of(filter);
        return CommonResponse.success(PageEntity.build(repository.findAll(pageable)));
    }

    default CommonResponse<T> getById(BaseRepository repository, Long id)
    {
        return CommonResponse.success(repository.findById(id));
    }

    default CommonResponse<T> saveOrUpdate(BaseRepository repository, T configure)
    {
        if (configure.getId() != null) {
            repository.findById(configure.getId())
                    .ifPresent(value -> NullAwareBeanUtils.copyNullProperties(value, configure));
        }
        return CommonResponse.success(repository.save(configure));
    }

    default CommonResponse<Long> deleteById(BaseRepository repository, Long id)
    {
        repository.deleteById(id);
        return CommonResponse.success(id);
    }

    default CommonResponse<T> findByCode(BaseRepository repository, String code)
    {
        return (CommonResponse<T>) repository.findByCode(code)
                .map(CommonResponse::success)
                .orElseGet(() -> CommonResponse.failure(String.format("Resource [ %s ] not found", code)));
    }
}
