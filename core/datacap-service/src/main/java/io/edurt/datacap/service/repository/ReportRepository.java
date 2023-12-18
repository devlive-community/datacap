package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.ReportEntity;
import io.edurt.datacap.service.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReportRepository
        extends PagingAndSortingRepository<ReportEntity, Long>
{
    Page<ReportEntity> findAllByUser(UserEntity user, Pageable pageable);
}
