package io.edurt.datacap.service.repository.metadata;

import io.edurt.datacap.service.entity.DatabaseEntity;
import io.edurt.datacap.service.entity.SourceEntity;
import io.edurt.datacap.service.repository.BaseRepository;

import java.util.List;

public interface DatabaseRepository
        extends BaseRepository<DatabaseEntity, Long>
{
    List<DatabaseEntity> findAllBySource(SourceEntity source);
}
