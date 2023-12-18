package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.ReportEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReportRepository
        extends PagingAndSortingRepository<ReportEntity, Long>
{
}
