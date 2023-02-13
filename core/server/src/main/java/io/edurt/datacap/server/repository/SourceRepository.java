package io.edurt.datacap.server.repository;

import io.edurt.datacap.server.entity.SourceEntity;
import io.edurt.datacap.server.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SourceRepository
        extends PagingAndSortingRepository<SourceEntity, Long>
{
    SourceEntity findByName(String name);

    Page<SourceEntity> findAllByUserOrPublishIsTrue(UserEntity user, Pageable pageable);

    Long countByUserOrPublishIsTrue(UserEntity user);
}
