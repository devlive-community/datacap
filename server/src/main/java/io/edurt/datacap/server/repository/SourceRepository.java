package io.edurt.datacap.server.repository;

import io.edurt.datacap.server.entity.SourceEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SourceRepository
        extends PagingAndSortingRepository<SourceEntity, Long>
{
    SourceEntity findByName(String name);
}
