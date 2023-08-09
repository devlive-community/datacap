package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.ScheduledEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ScheduledRepository
        extends PagingAndSortingRepository<ScheduledEntity, Long>
{
    List<ScheduledEntity> findAllByActiveIsTrueAndIsSystemIsTrue();
}
