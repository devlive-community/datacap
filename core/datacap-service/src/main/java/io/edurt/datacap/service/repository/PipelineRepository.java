package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.PipelineEntity;
import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.spi.executor.PipelineState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PipelineRepository
        extends PagingAndSortingRepository<PipelineEntity, Long>
{
    Page<PipelineEntity> findAllByUser(UserEntity user, Pageable pageable);

    List<PipelineEntity> findAllByStateIs(PipelineState state);
}
