package io.edurt.datacap.server.repository;

import io.edurt.datacap.server.entity.FunctionsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FunctionsRepository
        extends PagingAndSortingRepository<FunctionsEntity, Long>
{
    Page<FunctionsEntity> findAllByPluginContaining(Pageable pageable, String plugin);
}
