package io.edurt.datacap.server.repository;

import io.edurt.datacap.server.entity.ScheduledTaskEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ScheduledTaskRepository
        extends PagingAndSortingRepository<ScheduledTaskEntity, Long>
{
    List<ScheduledTaskEntity> findAllByActiveIsTrueAndIsSystemIsTrue();
}
