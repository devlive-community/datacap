package io.edurt.datacap.service.repository;

import io.edurt.datacap.executor.common.RunState;
import io.edurt.datacap.service.entity.PipelineEntity;
import io.edurt.datacap.service.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PipelineRepository
        extends BaseRepository<PipelineEntity, Long>
{
    Page<PipelineEntity> findAllByUser(UserEntity user, Pageable pageable);

    List<PipelineEntity> findAllByStateIn(List<RunState> state);
}
