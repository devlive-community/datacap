package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.FunctionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FunctionsRepository
        extends BaseRepository<FunctionEntity, Long>
{
    Page<FunctionEntity> findAllByPluginContaining(Pageable pageable, String plugin);
}
