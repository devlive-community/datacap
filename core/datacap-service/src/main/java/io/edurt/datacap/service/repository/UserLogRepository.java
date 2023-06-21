package io.edurt.datacap.service.repository;

import io.edurt.datacap.service.entity.UserEntity;
import io.edurt.datacap.service.entity.UserLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserLogRepository
        extends PagingAndSortingRepository<UserLogEntity, Long>
{
    Page<UserLogEntity> findAllByUserOrderByCreateTimeDesc(UserEntity user, Pageable pageable);
}
