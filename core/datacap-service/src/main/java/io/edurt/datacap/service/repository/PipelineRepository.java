package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.PipelineEntity;
import io.edurt.datacap.service.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PipelineRepository
        extends PagingAndSortingRepository<PipelineEntity, Long>
{
    Page<PipelineEntity> findAllByUser(UserEntity user, Pageable pageable);
}
