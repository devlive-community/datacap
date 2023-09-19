package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.ScheduledHistoryEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ScheduledHistoryRepository
        extends PagingAndSortingRepository<ScheduledHistoryEntity, Long>
{
}
