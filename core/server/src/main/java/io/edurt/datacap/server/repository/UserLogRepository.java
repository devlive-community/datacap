package io.edurt.datacap.server.repository;

import io.edurt.datacap.server.entity.UserEntity;
import io.edurt.datacap.server.entity.UserLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserLogRepository
        extends PagingAndSortingRepository<UserLogEntity, Long>
{
    Page<UserLogEntity> findAllByUserOrderByCreateTimeDesc(UserEntity user, Pageable pageable);
}
