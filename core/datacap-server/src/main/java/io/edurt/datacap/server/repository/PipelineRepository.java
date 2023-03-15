package io.edurt.datacap.server.repository;

import io.edurt.datacap.server.entity.PipelineEntity;
import io.edurt.datacap.server.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PipelineRepository
        extends PagingAndSortingRepository<PipelineEntity, Long>
{
    Page<PipelineEntity> findAllByUser(UserEntity user, Pageable pageable);
}
