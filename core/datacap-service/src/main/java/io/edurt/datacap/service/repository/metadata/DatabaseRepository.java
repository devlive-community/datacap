package io.edurt.datacap.service.repository.metadata;

import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.entity.metadata.DatabaseEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DatabaseRepository
        extends PagingAndSortingRepository<DatabaseEntity, Long>
{
    List<DatabaseEntity> findAllBySource(SourceEntity source);
}
