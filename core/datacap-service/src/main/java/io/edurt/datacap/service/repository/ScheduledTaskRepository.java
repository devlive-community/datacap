package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.ScheduledTaskEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ScheduledTaskRepository
        extends PagingAndSortingRepository<ScheduledTaskEntity, Long>
{
    List<ScheduledTaskEntity> findAllByActiveIsTrueAndIsSystemIsTrue();
}
