package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.ScheduledEntity;

import java.util.List;

public interface ScheduledRepository
        extends BaseRepository<ScheduledEntity, Long>
{
    List<ScheduledEntity> findAllByActiveIsTrueAndIsSystemIsTrue();
}
